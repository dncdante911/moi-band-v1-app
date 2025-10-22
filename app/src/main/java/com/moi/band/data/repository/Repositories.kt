package com.moi.band.data.repository

import com.moi.band.data.api.ApiService
import com.moi.band.data.model.*
import com.moi.band.util.Resource
import com.moi.band.util.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// ========== AUTH REPOSITORY ==========
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun login(username: String, password: String): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data
                authResponse?.let {
                    tokenManager.saveToken(it.token)
                    tokenManager.saveUserInfo(
                        it.user.id,
                        it.user.username,
                        it.user.email,
                        it.user.displayName
                    )
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(response.body()?.error ?: "Login failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun register(
        username: String,
        email: String,
        password: String,
        passwordConfirm: String,
        displayName: String
    ): Flow<Resource<AuthResponse>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.register(
                RegisterRequest(username, email, password, passwordConfirm, displayName)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data
                authResponse?.let {
                    tokenManager.saveToken(it.token)
                    tokenManager.saveUserInfo(
                        it.user.id,
                        it.user.username,
                        it.user.email,
                        it.user.displayName
                    )
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error(response.body()?.error ?: "Registration failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun logout() {
        tokenManager.clearAll()
    }

    suspend fun isLoggedIn(): Boolean = tokenManager.isLoggedIn()
}

// ========== ALBUM REPOSITORY ==========
class AlbumRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getAlbums(page: Int = 1): Flow<Resource<PaginatedResponse<Album>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAlbums(page, 10)
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to load albums"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun getAlbumDetail(id: Int): Flow<Resource<Album>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getAlbumDetail(id)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Album not found"))
            } else {
                emit(Resource.Error("Failed to load album"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}

// ========== TRACK REPOSITORY ==========
class TrackRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun searchTracks(query: String, page: Int = 1): Flow<Resource<PaginatedResponse<Track>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.searchTracks(query, page, 20)
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Search failed"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun playTrack(trackId: Int): Flow<Resource<Track>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            val response = api.playTrack("Bearer $token", TrackPlayRequest(trackId))
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Failed to record play"))
            } else {
                emit(Resource.Error("Failed to record play"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}

// ========== NEWS REPOSITORY ==========
class NewsRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getNews(page: Int = 1, category: String? = null): Flow<Resource<PaginatedResponse<News>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getNews(page, 10, category)
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to load news"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun getNewsDetail(id: Int): Flow<Resource<News>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getNewsDetail(id)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("News not found"))
            } else {
                emit(Resource.Error("Failed to load news"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}

// ========== GALLERY REPOSITORY ==========
class GalleryRepository @Inject constructor(
    private val api: ApiService
) {
    suspend fun getGallery(page: Int = 1, category: String? = null): Flow<Resource<PaginatedResponse<GalleryImage>>> = flow {
        emit(Resource.Loading())
        try {
            val response = api.getGallery(page, 12, category)
            if (response.isSuccessful && response.body()?.success == true) {
                emit(Resource.Success(response.body()!!))
            } else {
                emit(Resource.Error("Failed to load gallery"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}

// ========== CHAT REPOSITORY ==========
class ChatRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun getChatRooms(): Flow<Resource<List<ChatRoom>>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            val response = api.getChatRooms("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load rooms"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun getChatMessages(roomId: Int, limit: Int = 50, offset: Int = 0): Flow<Resource<List<ChatMessage>>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            val response = api.getChatMessages("Bearer $token", roomId, limit, offset)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Empty response"))
            } else {
                emit(Resource.Error("Failed to load messages"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun sendMessage(roomId: Int, message: String): Flow<Resource<ChatMessage>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            val response = api.sendMessage("Bearer $token", SendMessageRequest(roomId, message))
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Failed to send"))
            } else {
                emit(Resource.Error("Failed to send message"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}

// ========== USER REPOSITORY ==========
class UserRepository @Inject constructor(
    private val api: ApiService,
    private val tokenManager: TokenManager
) {
    suspend fun getProfile(): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            val response = api.getProfile("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Failed to load profile"))
            } else {
                emit(Resource.Error("Failed to load profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }

    suspend fun updateProfile(
        displayName: String?,
        bio: String?,
        theme: String?,
        notificationsEnabled: Boolean?
    ): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                emit(Resource.Error("Not authenticated"))
                return@flow
            }
            val request = ProfileUpdateRequest(displayName, bio, theme, notificationsEnabled)
            val response = api.updateProfile("Bearer $token", request)
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let {
                    theme?.let { t -> tokenManager.saveTheme(t) }
                    emit(Resource.Success(it))
                } ?: emit(Resource.Error("Failed to update"))
            } else {
                emit(Resource.Error("Failed to update profile"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Network error"))
        }
    }
}