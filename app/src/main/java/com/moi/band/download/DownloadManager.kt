package com.moi.band.download

import android.content.Context
import com.moi.band.BuildConfig
import com.moi.band.data.local.AppDatabase
import com.moi.band.data.local.DownloadedTrack
import com.moi.band.data.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

data class DownloadProgress(
    val trackId: Int,
    val progress: Float,
    val status: DownloadStatus
)

enum class DownloadStatus {
    IDLE,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    PAUSED
}

@Singleton
class DownloadManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val okHttpClient: OkHttpClient,
    private val database: AppDatabase
) {
    private val _downloadProgress = MutableStateFlow<Map<Int, DownloadProgress>>(emptyMap())
    val downloadProgress: StateFlow<Map<Int, DownloadProgress>> = _downloadProgress.asStateFlow()

    private val downloadDir: File by lazy {
        File(context.filesDir, "downloads").apply {
            if (!exists()) mkdirs()
        }
    }

    suspend fun downloadTrack(track: Track): Result<String> = withContext(Dispatchers.IO) {
        try {
            // Check if already downloaded
            val existing = database.downloadDao().getDownloadedTrack(track.id)
            if (existing != null && File(existing.localPath).exists()) {
                return@withContext Result.success(existing.localPath)
            }

            updateProgress(track.id, 0f, DownloadStatus.DOWNLOADING)

            val url = BuildConfig.BASE_URL + track.audioUrl
            val request = Request.Builder().url(url).build()
            val response = okHttpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                updateProgress(track.id, 0f, DownloadStatus.FAILED)
                return@withContext Result.failure(Exception("Download failed: ${response.code}"))
            }

            val fileName = "track_${track.id}.mp3"
            val file = File(downloadDir, fileName)

            response.body?.let { body ->
                val totalBytes = body.contentLength()
                var downloadedBytes = 0L

                FileOutputStream(file).use { output ->
                    body.byteStream().use { input ->
                        val buffer = ByteArray(8192)
                        var bytesRead: Int

                        while (input.read(buffer).also { bytesRead = it } != -1) {
                            output.write(buffer, 0, bytesRead)
                            downloadedBytes += bytesRead

                            if (totalBytes > 0) {
                                val progress = downloadedBytes.toFloat() / totalBytes
                                updateProgress(track.id, progress, DownloadStatus.DOWNLOADING)
                            }
                        }
                    }
                }

                // Save to database
                database.downloadDao().insertDownload(
                    DownloadedTrack(
                        trackId = track.id,
                        localPath = file.absolutePath
                    )
                )

                updateProgress(track.id, 1f, DownloadStatus.COMPLETED)
                Result.success(file.absolutePath)
            } ?: Result.failure(Exception("Empty response body"))

        } catch (e: Exception) {
            updateProgress(track.id, 0f, DownloadStatus.FAILED)
            Result.failure(e)
        }
    }

    suspend fun deleteDownload(trackId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val download = database.downloadDao().getDownloadedTrack(trackId)
            if (download != null) {
                File(download.localPath).delete()
                database.downloadDao().deleteDownload(download)
                updateProgress(trackId, 0f, DownloadStatus.IDLE)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    suspend fun isTrackDownloaded(trackId: Int): Boolean = withContext(Dispatchers.IO) {
        val download = database.downloadDao().getDownloadedTrack(trackId)
        download != null && File(download.localPath).exists()
    }

    suspend fun getDownloadedTrackPath(trackId: Int): String? = withContext(Dispatchers.IO) {
        val download = database.downloadDao().getDownloadedTrack(trackId)
        if (download != null && File(download.localPath).exists()) {
            download.localPath
        } else {
            null
        }
    }

    suspend fun getAllDownloads(): List<DownloadedTrack> = withContext(Dispatchers.IO) {
        database.downloadDao().getAllDownloadedTracks()
    }

    suspend fun clearAllDownloads() = withContext(Dispatchers.IO) {
        val downloads = database.downloadDao().getAllDownloadedTracks()
        downloads.forEach { download ->
            File(download.localPath).delete()
        }
        database.downloadDao().clearAll()
        _downloadProgress.value = emptyMap()
    }

    fun getDownloadProgress(trackId: Int): DownloadProgress? {
        return _downloadProgress.value[trackId]
    }

    private fun updateProgress(trackId: Int, progress: Float, status: DownloadStatus) {
        val current = _downloadProgress.value.toMutableMap()
        current[trackId] = DownloadProgress(trackId, progress, status)
        _downloadProgress.value = current
    }

    fun getDownloadSize(): Long {
        return downloadDir.listFiles()?.sumOf { it.length() } ?: 0L
    }

    fun formatSize(bytes: Long): String {
        val kb = bytes / 1024.0
        val mb = kb / 1024.0
        val gb = mb / 1024.0

        return when {
            gb >= 1 -> String.format("%.2f GB", gb)
            mb >= 1 -> String.format("%.2f MB", mb)
            kb >= 1 -> String.format("%.2f KB", kb)
            else -> "$bytes B"
        }
    }
}