package com.yeonberry.bookfinderapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.yeonberry.bookfinderapp.data.model.Book

interface SearchRepository {
    suspend fun searchBooks(q: String): LiveData<PagingData<Book>>
}