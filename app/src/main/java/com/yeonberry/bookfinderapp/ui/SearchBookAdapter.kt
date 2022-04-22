package com.yeonberry.bookfinderapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.databinding.ItemBookBinding

class SearchBookAdapter(private val onItemClick: (Book) -> Unit) :
    PagingDataAdapter<Book, SearchViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClick
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
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

private class SearchDiffCallback : DiffUtil.ItemCallback<Book>() {

    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}
