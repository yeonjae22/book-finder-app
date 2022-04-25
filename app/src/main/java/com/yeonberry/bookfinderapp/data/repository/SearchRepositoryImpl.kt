package com.yeonberry.bookfinderapp.data.repository

import com.yeonberry.bookfinderapp.api.SearchService
import com.yeonberry.bookfinderapp.data.model.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val api: SearchService) :
    SearchRepository {
    override suspend fun searchBooks(q: String, startIndex: Int): Response<SearchResponse> {
        return api.searchBooks(q, startIndex)
    }
}