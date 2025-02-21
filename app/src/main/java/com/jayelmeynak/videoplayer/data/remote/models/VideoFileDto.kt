package com.jayelmeynak.videoplayer.data.remote.models

import com.google.gson.annotations.SerializedName

data class VideoFileDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("width")
    val width: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("quality")
    val quality: String,
    @SerializedName("file_type")
    val fileType: String,
)