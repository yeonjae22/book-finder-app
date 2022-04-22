package com.yeonberry.bookfinderapp.data.model

sealed class SearchModel {
    data class TotalCountItem(val totalCount: Int) : SearchModel()
    data class BookItem(val book: Book) : SearchModel()
}