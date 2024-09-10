package com.skymilk.wallpaperapp.store.presentation.screen.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.skymilk.wallpaperapp.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter : RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    var onItemClickSearch: ((String) -> Unit)? = null //검색 시도 이벤트
    var onItemClickDelete: ((String) -> Unit)? = null //검색 이력 삭제 이벤트

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        return SearchHistoryViewHolder(
            ItemSearchHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class SearchHistoryViewHolder(val binding: ItemSearchHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            //텍스트 클릭 시 검색 처리
            binding.txtHistory.setOnClickListener {
                onItemClickSearch?.invoke(differ.currentList[absoluteAdapterPosition])
            }

            //아이콘 클릭 시 삭제 처리
            binding.btnDelete.setOnClickListener {
                onItemClickDelete?.invoke(differ.currentList[absoluteAdapterPosition])
            }
        }

        fun bind(history: String) {
            binding.txtHistory.text = history
        }

    }
}