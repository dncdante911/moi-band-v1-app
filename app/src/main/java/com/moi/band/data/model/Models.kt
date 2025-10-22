package com.moi.band.data.model

import com.google.gson.annotations.SerializedName

// ========== API Response Wrappers ==========
data class ApiResponse<T>(
    val success: Boolean,
    val code: Int,
    val message: String?,
    val data: T?,
    val error: String?,
    val timestamp: String?,
    val version: String?
)

data class PaginatedResponse<T>(
    val success: Boolean,
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val total: Int,
    val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("has_next") val hasNext: Boolean,
    @SerializedName("has_prev") val hasPrev: Boolean
)

// ========== User & Auth ==========
data class User(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("avatar_path") val avatarPath: String?,
    val bio: String?,
    val status: String?,
    @SerializedName("is_admin") val isAdmin: Boolean?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("last_seen") val lastSeen: String?,
    val theme: String?,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean?,
    val statistics: UserStatistics?
)

data class UserStatistics(
    val plays: Int,
    @SerializedName("room_messages") val roomMessages: Int,
    val favorites: Int
)

data class AuthResponse(
    val token: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: Int,
    val user: User
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    @SerializedName("password_confirm") val passwordConfirm: String,
    @SerializedName("display_name") val displayName: String
)

// ========== Album ==========
data class Album(
    val id: Int,
    val title: String,
    val description: String?,
    @SerializedName("coverImagePath") val coverImagePath: String?,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("track_count") val trackCount: Int,
    @SerializedName("total_duration") val totalDuration: Int?,
    @SerializedName("total_duration_formatted") val totalDurationFormatted: String?,
    @SerializedName("createdAt") val createdAt: String,
    val tracks: List<Track>?
)

// ========== Track ==========
data class Track(
    val id: Int,
    val title: String,
    @SerializedName("cover_image") val coverImage: String?,
    @SerializedName("audio_url") val audioUrl: String,
    val duration: Int,
    @SerializedName("duration_formatted") val durationFormatted: String,
    val views: Int,
    @SerializedName("created_at") val createdAt: String?,
    val album: TrackAlbum?,
    @SerializedName("played_by_user") val playedByUser: Boolean?
)

data class TrackAlbum(
    val id: Int,
    val title: String
)

data class TrackPlayRequest(
    @SerializedName("track_id") val trackId: Int
)

// ========== News ==========
data class News(
    val id: Int,
    val title: String,
    val content: String,
    val excerpt: String?,
    val category: String,
    @SerializedName("image_path") val imagePath: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("views_count") val viewsCount: Int?
)

// ========== Gallery ==========
data class GalleryImage(
    val id: Int,
    val title: String?,
    val description: String?,
    @SerializedName("image_path") val imagePath: String,
    @SerializedName("thumbnail_path") val thumbnailPath: String?,
    val category: String,
    @SerializedName("created_at") val createdAt: String
)

// ========== Chat ==========
data class ChatRoom(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String?,
    val icon: String?,
    @SerializedName("is_private") val isPrivate: Boolean,
    @SerializedName("messages_count") val messagesCount: Int?,
    @SerializedName("last_message") val lastMessage: LastMessage?
)

data class LastMessage(
    val text: String,
    val username: String,
    @SerializedName("created_at") val createdAt: String
)

data class ChatMessage(
    val id: Int,
    val message: String,
    @SerializedName("user_id") val userId: Int,
    val username: String,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("avatar_path") val avatarPath: String?,
    @SerializedName("created_at") val createdAt: String,
    val timestamp: String?
)

data class SendMessageRequest(
    @SerializedName("room_id") val roomId: Int,
    val message: String
)

// ========== Profile Update ==========
data class ProfileUpdateRequest(
    @SerializedName("display_name") val displayName: String?,
    val bio: String?,
    val theme: String?,
    @SerializedName("notifications_enabled") val notificationsEnabled: Boolean?
)