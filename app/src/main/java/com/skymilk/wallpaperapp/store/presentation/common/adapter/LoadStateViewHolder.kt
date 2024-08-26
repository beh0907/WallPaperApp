package com.skymilk.wallpaperapp.store.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemLoaderBinding

class LoadStateViewHolder(
    private val binding: ItemLoaderBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.btnRetry.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            if (loadState is LoadState.Error) {
                txtError.text = "다시 시도해주세요"
            }

            progressBar.isVisible = loadState is LoadState.Loading
            btnRetry.isVisible = loadState !is LoadState.Loading
            txtError.isVisible = loadState !is LoadState.Loading
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_loader, parent, false)
            val binding = ItemLoaderBinding.bind(view)
            return LoadStateViewHolder(binding, retry)
        }
    }
}