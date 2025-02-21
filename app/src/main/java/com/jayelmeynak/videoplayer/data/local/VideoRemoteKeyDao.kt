package com.jayelmeynak.videoplayer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jayelmeynak.videoplayer.data.local.models.VideoRemoteKey

@Dao
interface VideoRemoteKeyDao {
    @Query("SELECT * FROM video_remote_keys WHERE remoteKeyId = 0")
    suspend fun remoteKey(): VideoRemoteKey?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: VideoRemoteKey)

    @Query("DELETE FROM video_remote_keys")
    suspend fun clearRemoteKeys()
}