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
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.yeonberry.bookfinderapp.R
import com.yeonberry.bookfinderapp.databinding.ActivitySearchBookBinding
import com.yeonberry.bookfinderapp.ui.adapter.LoadStateAdapter
import com.yeonberry.bookfinderapp.util.VerticalSpaceItemDecoration
import com.yeonberry.bookfinderapp.util.dpToPx
import com.yeonberry.bookfinderapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBookBinding
    private val viewModel: SearchBookViewModel by viewModels()
    private lateinit var searchAdapter: SearchBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initListener()
    }

    private fun initAdapter() {
        searchAdapter = SearchBookAdapter { book ->
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(book.volumeInfo?.infoLink)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, R.string.browser_error_toast_message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.rvSearch.apply {
            adapter = searchAdapter.withLoadStateFooter(footer = LoadStateAdapter())
            addItemDecoration(VerticalSpaceItemDecoration(dpToPx(context, 16)))
        }

        searchAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && searchAdapter.itemCount < 1) {
                binding.rvSearch.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.rvSearch.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
            }

            binding.progressBar.isVisible = loadState.refresh is LoadState.Loading

            if (loadState.refresh is LoadState.Error) {
                Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
            }
        }

        searchAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.rvSearch.scrollToPosition(0)
                }
            }
        })
    }

    private fun initListener() {
        binding.ivSearch.setOnClickListener {
            getUserList()
        }

        binding.edtSearch.setOnKeyListener { _, keyCode, keyEvent ->
            if ((keyEvent.action == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                getUserList()
                true
            } else {
                false
            }
        }
    }

    private fun getUserList() {
        lifecycleScope.launch {
            viewModel.searchUsers(binding.edtSearch.text.toString())
                .observe(this@SearchBookActivity) {
                    searchAdapter.submitData(lifecycle, it)
                }
        }
        binding.edtSearch.hideKeyboard()
    }
}