package com.skymilk.wallpaperapp.store.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemWallPaperBinding
import com.skymilk.wallpaperapp.store.domain.model.Hit
import com.skymilk.wallpaperapp.utils.ImageUtil

class WallPaperAdapter : PagingDataAdapter<Hit, WallPaperAdapter.WallPaperViewHolder>(
    DiffUtilCallback()
) {
    var onItemClick: ((Hit) -> Unit)? = null

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


    class DiffUtilCallback : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }
    }


    inner class WallPaperViewHolder(val binding: ItemWallPaperBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hit: Hit) {
            binding.apply {
                Glide.with(itemView.context)
                    .asBitmap()
                    .load(hit.webformatURL)
                    .placeholder(ImageUtil.getShimmerDrawable())
                    .centerCrop()
                    .transition(BitmapTransitionOptions.withCrossFade(100))
                    .error(R.color.teal_200)
                    .into(imageWallPaper)

            }


            itemView.setOnClickListener {
                onItemClick?.invoke(hit)
            }
        }
    }
}