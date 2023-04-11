package com.rxsearch.data.api

import com.rxsearch.data.api.ImageApi

class ImageApiHelper(private val apiService: ImageApi) {

    suspend fun getSearchImages(searchText: String, sort: String, window: String) =
        apiService.getSearchImages(searchText, sort = "top", window = "all")
}