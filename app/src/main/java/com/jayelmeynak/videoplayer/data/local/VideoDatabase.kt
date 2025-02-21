package com.jayelmeynak.videoplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jayelmeynak.videoplayer.data.local.models.VideoEntity
import com.jayelmeynak.videoplayer.data.local.models.VideoRemoteKey

@Database(entities = [VideoEntity::class, VideoRemoteKey::class], version = 2)
abstract class VideoDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
    abstract fun remoteKeyDao(): VideoRemoteKeyDao
}