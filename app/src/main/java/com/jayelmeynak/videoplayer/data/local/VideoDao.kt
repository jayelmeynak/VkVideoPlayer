package com.jayelmeynak.videoplayer.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jayelmeynak.videoplayer.data.local.models.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY id ASC")
    fun getPopularVideosPaged(): PagingSource<Int, VideoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Query("DELETE FROM videos")
    suspend fun clearVideos()

    @Query("SELECT * FROM videos ORDER BY id ASC")
    fun getAllVideos(): Flow<List<VideoEntity>>
}