package com.yeonberry.bookfinderapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchBookViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    private val _bookList = MutableLiveData<List<Book>?>()
    val bookList: LiveData<List<Book>?> get() = _bookList

    val totalCount = MutableLiveData<Int?>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun searchBooks(q: String, startIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            val response = repository.searchBooks(q, startIndex)
            if (response.isSuccessful) {
                totalCount.postValue(response.body()?.totalItems)
                _bookList.postValue(response.body()?.items)
            } else {
                _errorMessage.value = response.code().toString()
            }
            _isLoading.postValue(false)
        }
    }
}