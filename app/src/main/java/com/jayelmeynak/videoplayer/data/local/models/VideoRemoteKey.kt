package com.jayelmeynak.videoplayer.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_remote_keys")
data class VideoRemoteKey(
    @PrimaryKey val remoteKeyId: Int = 0,
    val nextPage: Int?
)