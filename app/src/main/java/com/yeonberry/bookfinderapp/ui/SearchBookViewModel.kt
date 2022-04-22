package com.yeonberry.bookfinderapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    suspend fun searchUsers(q: String): LiveData<PagingData<Book>> {
        return repository.searchBooks(q)
    }
}