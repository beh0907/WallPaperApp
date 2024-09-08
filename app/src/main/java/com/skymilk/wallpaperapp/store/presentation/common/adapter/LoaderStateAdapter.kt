package com.skymilk.wallpaperapp.store.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemCategoryBinding
import com.skymilk.wallpaperapp.databinding.ItemLoaderBinding
import com.skymilk.wallpaperapp.store.domain.model.Category
import com.skymilk.wallpaperapp.store.presentation.util.ImageUtil

class LoaderStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<LoaderStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            ItemLoaderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(val binding: ItemLoaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    txtError.text = "다시 시도해주세요"
                }

                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState is LoadState.Error
                txtError.isVisible = loadState is LoadState.Error

                //재시도 버튼 이벤트
                btnRetry.setOnClickListener {
                    retry.invoke()
                }
            }
        }
    }
}