package com.moi.band.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.moi.band.data.model.Track
import com.moi.band.data.repository.TrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerManager: MusicPlayerManager,
    private val trackRepository: TrackRepository
) : ViewModel() {

    val currentTrack: StateFlow<Track?> = playerManager.currentTrack
    val isPlaying: StateFlow<Boolean> = playerManager.isPlaying
    val currentPosition: StateFlow<Long> = playerManager.currentPosition
    val duration: StateFlow<Long> = playerManager.duration
    val playbackState: StateFlow<Int> = playerManager.playbackState
    val playlist: StateFlow<List<Track>> = playerManager.playlist
    val currentIndex: StateFlow<Int> = playerManager.currentIndex

    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode: StateFlow<Int> = _repeatMode.asStateFlow()

    private val _shuffleEnabled = MutableStateFlow(false)
    val shuffleEnabled: StateFlow<Boolean> = _shuffleEnabled.asStateFlow()

    private val _showPlayer = MutableStateFlow(false)
    val showPlayer: StateFlow<Boolean> = _showPlayer.asStateFlow()

    fun playTrack(track: Track) {
        playerManager.playTrack(track)
        _showPlayer.value = true
        recordPlay(track.id)
    }

    fun playPlaylist(tracks: List<Track>, startIndex: Int = 0) {
        playerManager.playPlaylist(tracks, startIndex)
        _showPlayer.value = true
        recordPlay(tracks[startIndex].id)
    }

    fun playPause() {
        playerManager.playPause()
    }

    fun play() {
        playerManager.play()
    }

    fun pause() {
        playerManager.pause()
    }

    fun next() {
        playerManager.next()
        currentTrack.value?.let { recordPlay(it.id) }
    }

    fun previous() {
        playerManager.previous()
        currentTrack.value?.let { recordPlay(it.id) }
    }

    fun seekTo(position: Long) {
        playerManager.seekTo(position)
    }

    fun toggleRepeat() {
        val newMode = when (_repeatMode.value) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ALL
            Player.REPEAT_MODE_ALL -> Player.REPEAT_MODE_ONE
            else -> Player.REPEAT_MODE_OFF
        }
        _repeatMode.value = newMode
        playerManager.setRepeatMode(newMode)
    }

    fun toggleShuffle() {
        val newState = !_shuffleEnabled.value
        _shuffleEnabled.value = newState
        playerManager.setShuffleMode(newState)
    }

    fun showPlayer() {
        _showPlayer.value = true
    }

    fun hidePlayer() {
        _showPlayer.value = false
    }

    fun formatTime(milliseconds: Long): String {
        return playerManager.formatTime(milliseconds)
    }

    private fun recordPlay(trackId: Int) {
        viewModelScope.launch {
            trackRepository.playTrack(trackId).collect { /* Handle result */ }
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerManager.release()
    }
}