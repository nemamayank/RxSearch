package com.rxsearch.data.models

import com.rxsearch.data.models.Data

data class SearchImage(
    val `data`: List<Data>,
    val status: Int,
    val success: Boolean
)