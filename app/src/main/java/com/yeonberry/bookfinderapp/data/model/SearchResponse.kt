package com.yeonberry.bookfinderapp.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("items")
    val items: List<Book>?,
    @SerializedName("totalItems")
    val totalItems: Int?
)