package com.skymilk.wallpaperapp.store.presentation.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.skymilk.wallpaperapp.databinding.ItemWallPaperBinding
import com.skymilk.wallpaperapp.store.domain.model.Data
import com.skymilk.wallpaperapp.store.presentation.screen.main.MainFragmentDirections
import com.skymilk.wallpaperapp.utils.BlurHashDecoder
import com.skymilk.wallpaperapp.utils.Constants

class WallPaperAdapter : PagingDataAdapter<Data, WallPaperAdapter.WallPaperViewHolder>(
    DiffUtilCallback()
) {
    var onItemClick: ((Data, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallPaperViewHolder {
        return WallPaperViewHolder(
            ItemWallPaperBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WallPaperViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }


    class DiffUtilCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.blurHash == newItem.blurHash
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }


    inner class WallPaperViewHolder(val binding: ItemWallPaperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Data) {
            val blurHashDecodeDrawable = BlurHashDecoder.blurHashBitmap(itemView.resources, data)

            binding.apply {
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(data.smallImageUrl)
                    .centerCrop()
                    .transition(BitmapTransitionOptions.withCrossFade(100))
                    .placeholder(blurHashDecodeDrawable)
                    .error(blurHashDecodeDrawable)
                    .into(imageWallPaper)

            }


            itemView.setOnClickListener { view ->
                onItemClick?.invoke(data, view)
            }
        }
    }
}