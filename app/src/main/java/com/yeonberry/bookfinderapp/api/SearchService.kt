package com.yeonberry.bookfinderapp.api

import com.yeonberry.bookfinderapp.data.model.SearchResponse
import com.yeonberry.bookfinderapp.util.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") q: String,
        @Query("startIndex") startIndex: Int,
        @Query("maxResults") maxResults: Int? = PAGE_SIZE,
        @Query("printType") printType: String? = "all",
        @Query("orderBy") orderBy: String? = "relevance"
    ): Response<SearchResponse>
}