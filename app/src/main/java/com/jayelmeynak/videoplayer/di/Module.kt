package com.jayelmeynak.videoplayer.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.jayelmeynak.videoplayer.data.VideoRemoteMediator
import com.jayelmeynak.videoplayer.data.local.MIGRATION_1_2
import com.jayelmeynak.videoplayer.data.local.VideoDao
import com.jayelmeynak.videoplayer.data.local.VideoDatabase
import com.jayelmeynak.videoplayer.data.local.VideoRemoteKeyDao
import com.jayelmeynak.videoplayer.data.local.models.VideoEntity
import com.jayelmeynak.videoplayer.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {
    private const val BASE_URL = "https://api.pexels.com/videos/"
    private const val API_KEY = "xjZds2lpAzrSXlja5n7BmiYKEVKWZX3II6ZL8SAZ7issmO0ysf3D91bc"
    private const val TIMEOUT = 60L

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = { chain: okhttp3.Interceptor.Chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", API_KEY)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }
    @Provides
    @Singleton
    fun provideApi(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VideoDatabase {
        return Room.databaseBuilder(
            context,
            VideoDatabase::class.java,
            "video_database"
        )
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
    }

    @Provides
    @Singleton
    fun provideVideoDao(database: VideoDatabase): VideoDao {
        return database.videoDao()
    }

    @Provides
    @Singleton
    fun provideVideoRemoteDao(database: VideoDatabase): VideoRemoteKeyDao {
        return database.remoteKeyDao()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideVideoPager(videoDb: VideoDatabase, apiService: ApiService): Pager<Int, VideoEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            remoteMediator = VideoRemoteMediator(videoDb, apiService),
            pagingSourceFactory = { videoDb.videoDao().getPopularVideosPaged()}
        )
    }

}