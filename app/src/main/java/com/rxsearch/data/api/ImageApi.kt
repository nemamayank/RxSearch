package com.rxsearch.data.api

import com.rxsearch.data.models.SearchImage
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {
    @GET("gallery/search/")
    suspend fun getSearchImages(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("window") window: String
    ): SearchImage
}