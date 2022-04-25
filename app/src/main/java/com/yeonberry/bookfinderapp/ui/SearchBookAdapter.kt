package com.yeonberry.bookfinderapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yeonberry.bookfinderapp.R
import com.yeonberry.bookfinderapp.data.model.Book
import com.yeonberry.bookfinderapp.databinding.ItemBookBinding
import com.yeonberry.bookfinderapp.databinding.ItemTotalCountBinding

class SearchBookAdapter(private val onItemClick: (Book) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var itemList = mutableListOf<Book>()
    private var totalCount = 0

    fun setTotalCount(totalCount: Int) {
        this.totalCount = totalCount
    }

    fun addAllItem(items: List<Book>) {
        val startPoint = itemList.count() + 1
        itemList.addAll(items)
        notifyItemRangeChanged(startPoint, itemList.size)
    }

    fun replaceItem(items: List<Book>) {
        itemList.clear()
        itemList.addAll(items)
        notifyItemRangeChanged(0, itemList.size)
    }

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
        when (holder) {
            is TotalCountViewHolder -> {
                holder.bind(totalCount)
            }
            is SearchViewHolder -> {
                holder.bind(itemList[position - 1])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> R.layout.item_total_count
            else -> R.layout.item_book
        }
    }

    override fun getItemCount(): Int = itemList.size + 1
}

class TotalCountViewHolder(
    private val binding: ItemTotalCountBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(totalCount: Int) {
        binding.apply {
            tvTotalCount.text = String.format(
                tvTotalCount.context.getString(R.string.search_book_total_count),
                totalCount
            )
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
            tvAuthors.text = item.volumeInfo?.authors.toString().replace("[", "")
                .replace("]", "")
            tvPublishedDate.text = item.volumeInfo?.publishedDate
        }
    }
}