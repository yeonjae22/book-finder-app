package com.yeonberry.bookfinderapp.data.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("volumeInfo")
    val volumeInfo: Volume?
)

data class Volume(
    @SerializedName("title")
    val title: String?,
    @SerializedName("authors")
    val authors: List<String>?,
    @SerializedName("imageLinks")
    val imageLinks: ImageLinks?,
    @SerializedName("publishedDate")
    val publishedDate: String?,
    @SerializedName("infoLink")
    val infoLink: String?
)

data class ImageLinks(
    @SerializedName("smallThumbnail")
    val smallThumbnail: String?
)