package com.jayelmeynak.videoplayer.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey val id: Int,
    val width: Int,
    val height: Int,
    val videoUrl: String,
    val imageUrl: String,
    val userName: String,
    val userAvatarUrl: String,
)