package com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skymilk.wallpaperapp.databinding.ItemEditEmojiBinding
import com.skymilk.wallpaperapp.store.presentation.util.EmojiUtil

class EmojiAdapter(private val context: Context) :
    RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    //이모지 정보 추가
    private var emojiList: List<String> = EmojiUtil.getEmojis(context)

    var onItemClick: ((String) -> Unit)? = null

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