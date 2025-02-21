package com.jayelmeynak.videoplayer.data.remote.api

import com.jayelmeynak.videoplayer.data.remote.models.ResponsePopular
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("popular")
    suspend fun getPopulars(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 15
    ): Response<ResponsePopular>
}