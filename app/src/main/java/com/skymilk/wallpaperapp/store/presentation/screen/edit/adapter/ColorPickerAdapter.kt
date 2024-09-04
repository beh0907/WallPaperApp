package com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemColorPickerBinding

class ColorPickerAdapter(
    private val context: Context,
) : RecyclerView.Adapter<ColorPickerAdapter.ColorPickerViewHolder>() {

    private val colorList: MutableList<Int> = mutableListOf()

    var onItemClick: ((Int) -> Unit)? = null

    init {
        setUpTools()
    }

    //고정 색상 정보 하드코딩
    private fun setUpTools() {
        colorList.add(context.getColor(R.color.white))
        colorList.add(context.getColor(R.color.grey_700))
        colorList.add(context.getColor(R.color.red_700))
        colorList.add(context.getColor(R.color.pink_700))
        colorList.add(context.getColor(R.color.purple_700))
        colorList.add(context.getColor(R.color.dark_purple_700))
        colorList.add(context.getColor(R.color.indigo_700))
        colorList.add(context.getColor(R.color.light_blue_700))
        colorList.add(context.getColor(R.color.cyan_700))
        colorList.add(context.getColor(R.color.teal_700))
        colorList.add(context.getColor(R.color.green_700))
        colorList.add(context.getColor(R.color.light_green_700))
        colorList.add(context.getColor(R.color.lime_700))
        colorList.add(context.getColor(R.color.yellow_700))
        colorList.add(context.getColor(R.color.amber_700))
        colorList.add(context.getColor(R.color.orange_700))
        colorList.add(context.getColor(R.color.deep_orange_700))
        colorList.add(context.getColor(R.color.brown_700))
        colorList.add(context.getColor(R.color.blue_grey_700))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorPickerViewHolder {
        return ColorPickerViewHolder(
            ItemColorPickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = colorList.size

    override fun onBindViewHolder(holder: ColorPickerViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    inner class ColorPickerViewHolder(val binding: ItemColorPickerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(colorList[bindingAdapterPosition])
            }
        }

        fun bind(color: Int) {
            binding.apply {
                viewColor.setBackgroundColor(color)
            }
        }
    }
}