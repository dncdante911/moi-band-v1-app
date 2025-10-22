package com.moi.band.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.moi.band.BuildConfig
import com.moi.band.data.api.ApiService
import com.moi.band.data.repository.*
import com.moi.band.player.MusicPlayerManager
import com.moi.band.player.PlayerEventListener
import com.moi.band.util.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "moi_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideTokenManager(dataStore: DataStore<Preferences>): TokenManager {
        return TokenManager(dataStore)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build()
            chain.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL + "/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepository(apiService, tokenManager)
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(apiService: ApiService): AlbumRepository {
        return AlbumRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideTrackRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ): TrackRepository {
        return TrackRepository(apiService, tokenManager)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(apiService: ApiService): NewsRepository {
        return NewsRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideGalleryRepository(apiService: ApiService): GalleryRepository {
        return GalleryRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ): ChatRepository {
        return ChatRepository(apiService, tokenManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: ApiService,
        tokenManager: TokenManager
    ): UserRepository {
        return UserRepository(apiService, tokenManager)
    }

    // Player
    @Provides
    @Singleton
    fun providePlayerEventListener(): PlayerEventListener {
        return PlayerEventListener()
    }

    @Provides
    @Singleton
    fun provideMusicPlayerManager(
        @ApplicationContext context: Context
    ): MusicPlayerManager {
        return MusicPlayerManager(context)
    }

    // Room Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): com.moi.band.data.local.AppDatabase {
        return androidx.room.Room.databaseBuilder(
            context,
            com.moi.band.data.local.AppDatabase::class.java,
            "moi_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideAlbumDao(database: com.moi.band.data.local.AppDatabase) = database.albumDao()

    @Provides
    fun provideTrackDao(database: com.moi.band.data.local.AppDatabase) = database.trackDao()

    @Provides
    fun provideNewsDao(database: com.moi.band.data.local.AppDatabase) = database.newsDao()

    @Provides
    fun provideGalleryDao(database: com.moi.band.data.local.AppDatabase) = database.galleryDao()

    @Provides
    fun provideDownloadDao(database: com.moi.band.data.local.AppDatabase) = database.downloadDao()

    // Download Manager
    @Provides
    @Singleton
    fun provideDownloadManager(
        @ApplicationContext context: Context,
        okHttpClient: OkHttpClient,
        database: com.moi.band.data.local.AppDatabase
    ): com.moi.band.download.DownloadManager {
        return com.moi.band.download.DownloadManager(context, okHttpClient, database)
    }
}