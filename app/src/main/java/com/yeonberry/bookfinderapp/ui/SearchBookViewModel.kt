package com.yeonberry.bookfinderapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.paging.PagingData
import androidx.paging.insertHeaderItem
import androidx.paging.map
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.data.model.SearchModel
import com.yeonberry.bookfinderapp.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    suspend fun searchBooks(q: String): LiveData<PagingData<SearchModel>> {
        return repository.searchBooks(q)
            .map { pagingData ->
                pagingData.map<Book, SearchModel> { book -> SearchModel.BookItem(book) }
                    .insertHeaderItem(item = SearchModel.TotalCountItem(100))
            }

    }
}