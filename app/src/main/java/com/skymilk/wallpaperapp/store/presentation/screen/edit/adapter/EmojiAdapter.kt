package com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skymilk.wallpaperapp.databinding.ItemEditEmojiBinding
import com.skymilk.wallpaperapp.databinding.ItemEditFilterBinding
import com.skymilk.wallpaperapp.util.ImageUtil.getBitmapFromAsset
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoFilter

class EmojiAdapter : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    private var emojiList: MutableList<String> = mutableListOf()

    var onItemClick: ((String) -> Unit)? = null

    init {
        setUpEmojis()
    }

    //고정 이모지 정보 하드코딩
    private fun setUpEmojis() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        return EmojiViewHolder(
            ItemEditEmojiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = emojiList.size

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        holder.bind(emojiList[position])
    }

    inner class EmojiViewHolder(val binding: ItemEditEmojiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(emojiList[bindingAdapterPosition])
            }
        }

        fun bind(emoji: String) {
            binding.apply {
                txtEmoji.text = emoji
            }
        }
    }
}