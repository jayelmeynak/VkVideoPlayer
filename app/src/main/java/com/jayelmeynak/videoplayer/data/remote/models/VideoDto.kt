package com.jayelmeynak.videoplayer.data.remote.models

import com.google.gson.annotations.SerializedName

data class VideoDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("video_files")
    val videoFiles: List<VideoFileDto>,
    @SerializedName("video_pictures")
    val videoPictures: List<VideoPictureDto>,
)