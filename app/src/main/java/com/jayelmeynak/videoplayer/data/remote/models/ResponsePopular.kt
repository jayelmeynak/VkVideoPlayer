package com.jayelmeynak.videoplayer.data.remote.models

import com.google.gson.annotations.SerializedName

data class ResponsePopular(
    @SerializedName("page")
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("videos")
    val videos: List<VideoDto>
)