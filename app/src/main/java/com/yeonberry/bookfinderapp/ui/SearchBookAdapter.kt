package com.yeonberry.bookfinderapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yeonberry.bookfinderapp.R
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.data.model.SearchModel
import com.yeonberry.bookfinderapp.databinding.ItemBookBinding
import com.yeonberry.bookfinderapp.databinding.ItemTotalCountBinding

class SearchBookAdapter(private val onItemClick: (Book) -> Unit) :
    PagingDataAdapter<SearchModel, RecyclerView.ViewHolder>(SearchModelComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_total_count -> {
                TotalCountViewHolder(
                    ItemTotalCountBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                SearchViewHolder(
                    ItemBookBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ), onItemClick
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val searchModel = getItem(position)) {
            is SearchModel.TotalCountItem -> {
                val viewHolder = holder as TotalCountViewHolder
                viewHolder.bind(searchModel.totalCount)
            }
            is SearchModel.BookItem -> {
                val viewHolder = holder as SearchViewHolder
                viewHolder.bind(searchModel.book)
            }
            else -> throw Exception("")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SearchModel.TotalCountItem -> R.layout.item_total_count
            is SearchModel.BookItem -> R.layout.item_book
            null -> throw IllegalStateException("Unknown view")
        }
    }
}

class TotalCountViewHolder(
    private val binding: ItemTotalCountBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(totalCount: Int) {
        binding.apply {
            tvTotalCount.text = totalCount.toString()
        }
    }
}

class SearchViewHolder(
    private val binding: ItemBookBinding, val onItemClick: (Book) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Book) {
        binding.apply {
            root.setOnClickListener {
                onItemClick(item)
            }
            Glide.with(ivImage)
                .load(item.volumeInfo?.imageLinks?.smallThumbnail)
                .into(ivImage)
            tvTitle.text = item.volumeInfo?.title
            tvAuthors.text = item.volumeInfo?.authors.toString()
            tvPublishedDate.text = item.volumeInfo?.publishedDate
        }
    }
}

object SearchModelComparator : DiffUtil.ItemCallback<SearchModel>() {
    override fun areItemsTheSame(
        oldItem: SearchModel,
        newItem: SearchModel
    ): Boolean {
        val isSameTotalCount = false

        val isSameBookItem = oldItem is SearchModel.BookItem
                && newItem is SearchModel.BookItem
                && oldItem == newItem

        return isSameTotalCount || isSameBookItem
    }

    override fun areContentsTheSame(
        oldItem: SearchModel,
        newItem: SearchModel
    ) = oldItem == newItem
}