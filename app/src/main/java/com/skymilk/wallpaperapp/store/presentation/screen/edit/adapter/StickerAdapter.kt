package com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemEditStickerBinding

class StickerAdapter : RecyclerView.Adapter<StickerAdapter.StickerViewHolder>() {

    private var stickerList: MutableList<Int> = mutableListOf()

    var onItemClick: ((Bitmap) -> Unit)? = null

    init {
        setUpStickers()
    }

    //고정 이모지 정보 하드코딩
    private fun setUpStickers() {
        stickerList.add(R.drawable.aa)
        stickerList.add(R.drawable.bb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        return StickerViewHolder(
            ItemEditStickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = stickerList.size

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bind(stickerList[position])
    }

    inner class StickerViewHolder(val binding: ItemEditStickerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(
                    BitmapFactory.decodeResource(
                        itemView.resources,
                        stickerList[bindingAdapterPosition]
                    )
                )
            }
        }

        fun bind(sticker: Int) {
            binding.apply {
                Glide.with(binding.root)
                    .load(sticker)
                    .into(imageSticker)
            }
        }
    }
}