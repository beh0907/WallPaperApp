package com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemEditToolBinding
import com.skymilk.wallpaperapp.store.presentation.screen.edit.ToolModel
import com.skymilk.wallpaperapp.store.presentation.screen.edit.ToolType

class ToolAdapter : RecyclerView.Adapter<ToolAdapter.ToolViewHolder>() {

    private var photoToolList: MutableList<ToolModel> = mutableListOf()

    var onItemClick: ((ToolType) -> Unit)? = null

    init {
        setUpTools()
    }

    //고정 툴 정보 하드코딩
    private fun setUpTools() {
        photoToolList.add(ToolModel("그리기", R.drawable.ic_brush, ToolType.BRUSH))
        photoToolList.add(ToolModel("텍스트", R.drawable.ic_text, ToolType.TEXT))
        photoToolList.add(ToolModel("지우개", R.drawable.ic_eraser, ToolType.ERASER))
        photoToolList.add(ToolModel("필터", R.drawable.ic_photo_filter, ToolType.FILTER))
        photoToolList.add(ToolModel("이모지", R.drawable.ic_insert_emoticon, ToolType.EMOJI))
        photoToolList.add(ToolModel("스티커", R.drawable.ic_sticker, ToolType.STICKER))
        photoToolList.add(ToolModel("자르기", R.drawable.ic_crop, ToolType.CROP))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolViewHolder {
        return ToolViewHolder(
            ItemEditToolBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = photoToolList.size

    override fun onBindViewHolder(holder: ToolViewHolder, position: Int) {
        holder.bind(photoToolList[position])
    }

    inner class ToolViewHolder(val binding: ItemEditToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(photoToolList[bindingAdapterPosition].toolType)
            }
        }

        fun bind(toolModel: ToolModel) {
            binding.apply {
                txtTool.text = toolModel.toolName

                Glide.with(itemView.context)
                    .load(toolModel.toolIcon)
                    .into(imageTool)
            }
        }
    }
}