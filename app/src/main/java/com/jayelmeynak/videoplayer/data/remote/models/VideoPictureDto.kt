package com.jayelmeynak.videoplayer.data.remote.models

import com.google.gson.annotations.SerializedName

data class VideoPictureDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("nr")
    val nr: Int,
    @SerializedName("picture")
    val picture: String
)