package com.yeonberry.bookfinderapp.api

import com.yeonberry.bookfinderapp.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") q: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int,
        @Query("printType") printType: String? = "all",
        @Query("orderBy") orderBy: String? = "relevance"
    ): SearchResponse
}