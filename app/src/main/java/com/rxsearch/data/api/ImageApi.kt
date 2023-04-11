package com.rxsearch.data.api

import com.rxsearch.data.models.SearchImage
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageApi {

    @GET("gallery/search/")
    fun getImagesRx(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("window") window: String
    ): Observable<SearchImage>
}