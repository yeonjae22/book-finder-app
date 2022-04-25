package com.yeonberry.bookfinderapp.data.repository

import com.yeonberry.bookfinderapp.data.model.SearchResponse
import retrofit2.Response

interface SearchRepository {
    suspend fun searchBooks(q: String, startIndex: Int): Response<SearchResponse>
}