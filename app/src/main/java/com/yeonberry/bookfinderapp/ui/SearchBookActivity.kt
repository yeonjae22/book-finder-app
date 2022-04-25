package com.yeonberry.bookfinderapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yeonberry.bookfinderapp.R
import com.yeonberry.bookfinderapp.databinding.ActivitySearchBookBinding
import com.yeonberry.bookfinderapp.util.PAGE_SIZE
import com.yeonberry.bookfinderapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBookBinding
    private val viewModel: SearchBookViewModel by viewModels()
    private lateinit var searchAdapter: SearchBookAdapter
    private var page = 1
    private var isPaging = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initListener()
        showBookList()
        showErrorMessage()
        setProgressBar()
    }

    private fun initAdapter() {
        searchAdapter = SearchBookAdapter { book ->
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(book.volumeInfo?.infoLink)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.rvSearch.apply {
            adapter = searchAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        val position =
                            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1

                        if (page * PAGE_SIZE - 1 < position && searchAdapter.itemCount <= viewModel.totalCount.value ?: 0) {
                            page = (position) / PAGE_SIZE + 1
                            isPaging = true
                            searchBooks()
                        }
                    }
                }
            })
        }
    }

    private fun initListener() {
        binding.ivSearch.setOnClickListener {
            page = 1
            searchBooks()
        }

        binding.edtSearch.setOnKeyListener { _, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                page = 1
                searchBooks()
                true
            } else {
                false
            }
        }
    }

    private fun searchBooks() {
        viewModel.searchBooks(binding.edtSearch.text.toString(), page)
        binding.edtSearch.hideKeyboard()
    }

    private fun showBookList() {
        viewModel.bookList.observe(this) { bookList ->
            if (bookList.isNullOrEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvSearch.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvSearch.visibility = View.VISIBLE

                if (isPaging) {
                    searchAdapter.addAllItem(bookList)
                } else {
                    searchAdapter.replaceItem(bookList)
                    searchAdapter.setTotalCount(viewModel.totalCount.value ?: 0)
                }
            }
            isPaging = false
        }
    }

    private fun showErrorMessage() {
        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setProgressBar() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }
}