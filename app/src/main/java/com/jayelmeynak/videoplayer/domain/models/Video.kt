package com.jayelmeynak.videoplayer.domain.models

data class Video (
    val id: Int,
    val width: Int,
    val height: Int,
    val videoUrl: String,
    val imageUrl: String,
    val userName: String,
    val userAvatarUrl: String,
)

