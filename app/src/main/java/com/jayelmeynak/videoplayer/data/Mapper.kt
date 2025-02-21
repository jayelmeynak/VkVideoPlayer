package com.jayelmeynak.videoplayer.data

import com.jayelmeynak.videoplayer.data.local.models.VideoEntity
import com.jayelmeynak.videoplayer.data.remote.models.VideoDto
import com.jayelmeynak.videoplayer.domain.models.Video


fun VideoDto.toVideoEntity() = VideoEntity(
    id = id,
    width = width,
    height = height,
    videoUrl = videoFiles.maxBy { it.width * it.height }.link,
    imageUrl = image,
    userName = user.name,
    userAvatarUrl = user.url
)

fun VideoEntity.toVideo() = Video(
    id = id,
    width = width,
    height = height,
    videoUrl = videoUrl,
    imageUrl = imageUrl,
    userName = userName,
    userAvatarUrl = userAvatarUrl
)

fun VideoDto.toVideo() = Video(
    id = id,
    width = width,
    height = height,
    videoUrl = videoFiles.maxBy { it.width * it.height }.link,
    imageUrl = image,
    userName = user.name,
    userAvatarUrl = user.url
)