package com.yeonberry.bookfinderapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.yeonberry.bookfinderapp.api.SearchService
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.data.source.SearchPagingSource
import com.yeonberry.bookfinderapp.util.PAGE_SIZE
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val api: SearchService) :
    SearchRepository {
    override suspend fun searchBooks(q: String): LiveData<PagingData<Book>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { SearchPagingSource(api, q) }
        ).liveData
    }
}