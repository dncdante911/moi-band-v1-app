package com.moi.band.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val DISPLAY_NAME_KEY = stringPreferencesKey("display_name")
        private val THEME_KEY = stringPreferencesKey("theme")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        return dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }.first()
    }

    fun getTokenFlow(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[TOKEN_KEY]
        }
    }

    suspend fun saveUserInfo(userId: Int, username: String, email: String, displayName: String) {
        dataStore.edit { prefs ->
            prefs[USER_ID_KEY] = userId
            prefs[USERNAME_KEY] = username
            prefs[EMAIL_KEY] = email
            prefs[DISPLAY_NAME_KEY] = displayName
        }
    }

    suspend fun getUserId(): Int? {
        return dataStore.data.map { prefs ->
            prefs[USER_ID_KEY]
        }.first()
    }

    suspend fun getUsername(): String? {
        return dataStore.data.map { prefs ->
            prefs[USERNAME_KEY]
        }.first()
    }

    suspend fun saveTheme(theme: String) {
        dataStore.edit { prefs ->
            prefs[THEME_KEY] = theme
        }
    }

    fun getThemeFlow(): Flow<String?> {
        return dataStore.data.map { prefs ->
            prefs[THEME_KEY] ?: "dark"
        }
    }

    suspend fun clearAll() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    suspend fun getAuthHeader(): String {
        return "Bearer ${getToken()}"
    }
}