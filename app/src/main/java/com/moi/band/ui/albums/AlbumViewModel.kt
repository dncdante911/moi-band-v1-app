package com.moi.band.ui.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moi.band.data.model.Album
import com.moi.band.data.model.PaginatedResponse
import com.moi.band.data.repository.AlbumRepository
import com.moi.band.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : ViewModel() {

    private val _albumsState = MutableStateFlow<Resource<PaginatedResponse<Album>>?>(null)
    val albumsState: StateFlow<Resource<PaginatedResponse<Album>>?> = _albumsState.asStateFlow()

    private val _albumDetailState = MutableStateFlow<Resource<Album>?>(null)
    val albumDetailState: StateFlow<Resource<Album>?> = _albumDetailState.asStateFlow()

    private var currentPage = 1
    private val allAlbums = mutableListOf<Album>()

    init {
        loadAlbums()
    }

    fun loadAlbums(refresh: Boolean = false) {
        if (refresh) {
            currentPage = 1
            allAlbums.clear()
        }

        viewModelScope.launch {
            albumRepository.getAlbums(currentPage).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        resource.data?.let { response ->
                            allAlbums.addAll(response.data)
                            _albumsState.value = Resource.Success(
                                response.copy(data = allAlbums.toList())
                            )
                        }
                    }
                    is Resource.Error -> {
                        _albumsState.value = resource
                    }
                    is Resource.Loading -> {
                        _albumsState.value = resource
                    }
                }
            }
        }
    }

    fun loadMoreAlbums() {
        val current = _albumsState.value
        if (current is Resource.Success && current.data?.pagination?.hasNext == true) {
            currentPage++
            loadAlbums()
        }
    }

    fun getAlbumDetail(albumId: Int) {
        viewModelScope.launch {
            albumRepository.getAlbumDetail(albumId).collect {
                _albumDetailState.value = it
            }
        }
    }

    fun clearAlbumDetail() {
        _albumDetailState.value = null
    }
}