package com.jayelmeynak.videoplayer.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.jayelmeynak.videoplayer.data.local.VideoDatabase
import com.jayelmeynak.videoplayer.data.local.models.VideoEntity
import com.jayelmeynak.videoplayer.data.local.models.VideoRemoteKey
import com.jayelmeynak.videoplayer.data.remote.api.ApiService
import com.jayelmeynak.videoplayer.data.remote.models.ResponsePopular
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class VideoRemoteMediator(
    private val videoDb: VideoDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, VideoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VideoEntity>
    ): MediatorResult {
        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = videoDb.remoteKeyDao().remoteKey()
                    if (remoteKey?.nextPage == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    } else {
                        remoteKey.nextPage
                    }
                }
            }
            val result = apiService.getPopulars(page = loadKey, perPage = state.config.pageSize)
            if (result.isSuccessful) {
                val responsePopular: ResponsePopular = result.body() ?: ResponsePopular(page = loadKey, perPage = state.config.pageSize, videos = emptyList())
                val videosDto = responsePopular.videos
                val nextPage = responsePopular.page + 1
                videoDb.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        videoDb.videoDao().clearVideos()
                        videoDb.remoteKeyDao().clearRemoteKeys()
                    }
                    val videoEntities = videosDto.map { it.toVideoEntity() }
                    videoDb.videoDao().insertVideos(videoEntities)
                    videoDb.remoteKeyDao().insertRemoteKey(
                        VideoRemoteKey(remoteKeyId = 0, nextPage = if(videosDto.isEmpty()) null else nextPage)
                    )
                }

                return MediatorResult.Success(endOfPaginationReached = videosDto.isEmpty())
            } else {
                Log.d("MyLog", "Error: ${result.code()} ${result.message()}")
                return MediatorResult.Error(HttpException(result))
            }
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}
