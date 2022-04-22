package com.yeonberry.bookfinderapp.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yeonberry.bookfinderapp.api.SearchService
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.util.PAGE_SIZE

class SearchPagingSource(
    private val backend: SearchService,
    private val query: String
) : PagingSource<Int, Book>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Book> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response =
                backend.searchBooks(
                    q = query,
                    startIndex = nextPageNumber,
                    maxResults = params.loadSize
                )
            LoadResult.Page(
                data = response.items,
                prevKey = if (nextPageNumber == 1) null else nextPageNumber - 1,
                nextKey = if (response.items.isEmpty()) null else nextPageNumber + (params.loadSize / PAGE_SIZE)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return null
    }
}