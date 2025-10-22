package com.moi.band.data.local

import androidx.room.*
import com.moi.band.data.model.Album
import com.moi.band.data.model.Track
import com.moi.band.data.model.News
import com.moi.band.data.model.GalleryImage

// ========== ENTITIES ==========
@Entity(tableName = "albums_cache")
data class AlbumEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val coverImagePath: String?,
    val releaseDate: String,
    val trackCount: Int,
    val totalDuration: Int?,
    val totalDurationFormatted: String?,
    val createdAt: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "tracks_cache")
data class TrackEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val coverImage: String?,
    val audioUrl: String,
    val duration: Int,
    val durationFormatted: String,
    val views: Int,
    val createdAt: String?,
    val albumId: Int?,
    val albumTitle: String?,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "news_cache")
data class NewsEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val content: String,
    val excerpt: String?,
    val category: String,
    val imagePath: String?,
    val createdAt: String,
    val updatedAt: String?,
    val viewsCount: Int?,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "gallery_cache")
data class GalleryImageEntity(
    @PrimaryKey val id: Int,
    val title: String?,
    val description: String?,
    val imagePath: String,
    val thumbnailPath: String?,
    val category: String,
    val createdAt: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "downloaded_tracks")
data class DownloadedTrack(
    @PrimaryKey val trackId: Int,
    val localPath: String,
    val downloadedAt: Long = System.currentTimeMillis()
)

// ========== DAOs ==========
@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums_cache ORDER BY releaseDate DESC")
    suspend fun getAllAlbums(): List<AlbumEntity>

    @Query("SELECT * FROM albums_cache WHERE id = :albumId")
    suspend fun getAlbumById(albumId: Int): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Query("DELETE FROM albums_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpiredAlbums(expiryTime: Long)

    @Query("DELETE FROM albums_cache")
    suspend fun clearAll()
}

@Dao
interface TrackDao {
    @Query("SELECT * FROM tracks_cache WHERE albumId = :albumId")
    suspend fun getTracksByAlbum(albumId: Int): List<TrackEntity>

    @Query("SELECT * FROM tracks_cache WHERE id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackEntity?

    @Query("SELECT * FROM tracks_cache WHERE title LIKE '%' || :query || '%'")
    suspend fun searchTracks(query: String): List<TrackEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM tracks_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpiredTracks(expiryTime: Long)

    @Query("DELETE FROM tracks_cache")
    suspend fun clearAll()
}

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_cache ORDER BY createdAt DESC")
    suspend fun getAllNews(): List<NewsEntity>

    @Query("SELECT * FROM news_cache WHERE category = :category ORDER BY createdAt DESC")
    suspend fun getNewsByCategory(category: String): List<NewsEntity>

    @Query("SELECT * FROM news_cache WHERE id = :newsId")
    suspend fun getNewsById(newsId: Int): NewsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(newsList: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleNews(news: NewsEntity)

    @Query("DELETE FROM news_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpiredNews(expiryTime: Long)

    @Query("DELETE FROM news_cache")
    suspend fun clearAll()
}

@Dao
interface GalleryDao {
    @Query("SELECT * FROM gallery_cache ORDER BY createdAt DESC")
    suspend fun getAllImages(): List<GalleryImageEntity>

    @Query("SELECT * FROM gallery_cache WHERE category = :category ORDER BY createdAt DESC")
    suspend fun getImagesByCategory(category: String): List<GalleryImageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<GalleryImageEntity>)

    @Query("DELETE FROM gallery_cache WHERE cachedAt < :expiryTime")
    suspend fun deleteExpiredImages(expiryTime: Long)

    @Query("DELETE FROM gallery_cache")
    suspend fun clearAll()
}

@Dao
interface DownloadDao {
    @Query("SELECT * FROM downloaded_tracks WHERE trackId = :trackId")
    suspend fun getDownloadedTrack(trackId: Int): DownloadedTrack?

    @Query("SELECT * FROM downloaded_tracks")
    suspend fun getAllDownloadedTracks(): List<DownloadedTrack>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDownload(download: DownloadedTrack)

    @Delete
    suspend fun deleteDownload(download: DownloadedTrack)

    @Query("DELETE FROM downloaded_tracks")
    suspend fun clearAll()
}

// ========== DATABASE ==========
@Database(
    entities = [
        AlbumEntity::class,
        TrackEntity::class,
        NewsEntity::class,
        GalleryImageEntity::class,
        DownloadedTrack::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun trackDao(): TrackDao
    abstract fun newsDao(): NewsDao
    abstract fun galleryDao(): GalleryDao
    abstract fun downloadDao(): DownloadDao
}

// ========== MAPPERS ==========
fun Album.toEntity() = AlbumEntity(
    id = id,
    title = title,
    description = description,
    coverImagePath = coverImagePath,
    releaseDate = releaseDate,
    trackCount = trackCount,
    totalDuration = totalDuration,
    totalDurationFormatted = totalDurationFormatted,
    createdAt = createdAt
)

fun AlbumEntity.toAlbum() = Album(
    id = id,
    title = title,
    description = description,
    coverImagePath = coverImagePath,
    releaseDate = releaseDate,
    trackCount = trackCount,
    totalDuration = totalDuration,
    totalDurationFormatted = totalDurationFormatted,
    createdAt = createdAt,
    tracks = null
)

fun Track.toEntity() = TrackEntity(
    id = id,
    title = title,
    coverImage = coverImage,
    audioUrl = audioUrl,
    duration = duration,
    durationFormatted = durationFormatted,
    views = views,
    createdAt = createdAt,
    albumId = album?.id,
    albumTitle = album?.title
)

fun TrackEntity.toTrack() = Track(
    id = id,
    title = title,
    coverImage = coverImage,
    audioUrl = audioUrl,
    duration = duration,
    durationFormatted = durationFormatted,
    views = views,
    createdAt = createdAt,
    album = if (albumId != null) com.moi.band.data.model.TrackAlbum(albumId, albumTitle ?: "") else null,
    playedByUser = null
)

fun News.toEntity() = NewsEntity(
    id = id,
    title = title,
    content = content,
    excerpt = excerpt,
    category = category,
    imagePath = imagePath,
    createdAt = createdAt,
    updatedAt = updatedAt,
    viewsCount = viewsCount
)

fun NewsEntity.toNews() = News(
    id = id,
    title = title,
    content = content,
    excerpt = excerpt,
    category = category,
    imagePath = imagePath,
    createdAt = createdAt,
    updatedAt = updatedAt,
    viewsCount = viewsCount
)

fun GalleryImage.toEntity() = GalleryImageEntity(
    id = id,
    title = title,
    description = description,
    imagePath = imagePath,
    thumbnailPath = thumbnailPath,
    category = category,
    createdAt = createdAt
)

fun GalleryImageEntity.toGalleryImage() = GalleryImage(
    id = id,
    title = title,
    description = description,
    imagePath = imagePath,
    thumbnailPath = thumbnailPath,
    category = category,
    createdAt = createdAt
)