package com.moi.band.data.api

import com.moi.band.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // ========== AUTH ==========
    @POST("auth/login.php")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<AuthResponse>>
    
    @POST("auth/register.php")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<AuthResponse>>
    
    // ========== USER ==========
    @GET("user/profile.php")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ApiResponse<User>>
    
    @PUT("user/profile.php")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: ProfileUpdateRequest
    ): Response<ApiResponse<User>>
    
    // ========== ALBUMS ==========
    @GET("albums/list.php")
    suspend fun getAlbums(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10
    ): Response<PaginatedResponse<Album>>
    
    @GET("albums/detail.php")
    suspend fun getAlbumDetail(@Query("id") id: Int): Response<ApiResponse<Album>>
    
    // ========== TRACKS ==========
    @GET("tracks/list.php")
    suspend fun getTracks(
        @Query("album_id") albumId: Int,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<PaginatedResponse<Track>>
    
    @POST("tracks/play.php")
    suspend fun playTrack(
        @Header("Authorization") token: String,
        @Body request: TrackPlayRequest
    ): Response<ApiResponse<Track>>
    
    @GET("tracks/search.php")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<PaginatedResponse<Track>>
    
    // ========== NEWS ==========
    @GET("news/list.php")
    suspend fun getNews(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("category") category: String? = null
    ): Response<PaginatedResponse<News>>
    
    @GET("news/detail.php")
    suspend fun getNewsDetail(@Query("id") id: Int): Response<ApiResponse<News>>
    
    // ========== GALLERY ==========
    @GET("gallery/list.php")
    suspend fun getGallery(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 12,
        @Query("category") category: String? = null
    ): Response<PaginatedResponse<GalleryImage>>
    
    // ========== CHAT ==========
    @GET("chat/rooms.php")
    suspend fun getChatRooms(@Header("Authorization") token: String): Response<ApiResponse<List<ChatRoom>>>
    
    @GET("chat/messages.php")
    suspend fun getChatMessages(
        @Header("Authorization") token: String,
        @Query("room_id") roomId: Int,
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): Response<ApiResponse<List<ChatMessage>>>
    
    @POST("chat/send.php")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Body request: SendMessageRequest
    ): Response<ApiResponse<ChatMessage>>
}