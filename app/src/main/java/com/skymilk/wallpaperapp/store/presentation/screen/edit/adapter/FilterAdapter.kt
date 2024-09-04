package com.skymilk.wallpaperapp.store.presentation.screen.edit.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skymilk.wallpaperapp.databinding.ItemEditFilterBinding
import com.skymilk.wallpaperapp.util.ImageUtil.getBitmapFromAsset
import ja.burhanrashid52.photoeditor.PhotoFilter

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private var photoFilterList: MutableList<Pair<String, PhotoFilter>> = mutableListOf()

    var onItemClick: ((PhotoFilter) -> Unit)? = null

    init {
        setUpFilters()
    }

    //고정 필터 정보 하드코딩
    private fun setUpFilters() {
        photoFilterList.add(Pair("filters/original.jpg", PhotoFilter.NONE))
        photoFilterList.add(Pair("filters/auto_fix.png", PhotoFilter.AUTO_FIX))
        photoFilterList.add(Pair("filters/brightness.png", PhotoFilter.BRIGHTNESS))
        photoFilterList.add(Pair("filters/contrast.png", PhotoFilter.CONTRAST))
        photoFilterList.add(Pair("filters/documentary.png", PhotoFilter.DOCUMENTARY))
        photoFilterList.add(Pair("filters/dual_tone.png", PhotoFilter.DUE_TONE))
        photoFilterList.add(Pair("filters/fill_light.png", PhotoFilter.FILL_LIGHT))
        photoFilterList.add(Pair("filters/fish_eye.png", PhotoFilter.FISH_EYE))
        photoFilterList.add(Pair("filters/grain.png", PhotoFilter.GRAIN))
        photoFilterList.add(Pair("filters/gray_scale.png", PhotoFilter.GRAY_SCALE))
        photoFilterList.add(Pair("filters/lomish.png", PhotoFilter.LOMISH))
        photoFilterList.add(Pair("filters/negative.png", PhotoFilter.NEGATIVE))
        photoFilterList.add(Pair("filters/posterize.png", PhotoFilter.POSTERIZE))
        photoFilterList.add(Pair("filters/saturate.png", PhotoFilter.SATURATE))
        photoFilterList.add(Pair("filters/sepia.png", PhotoFilter.SEPIA))
        photoFilterList.add(Pair("filters/temprature.png", PhotoFilter.TEMPERATURE))
        photoFilterList.add(Pair("filters/tint.png", PhotoFilter.TINT))
        photoFilterList.add(Pair("filters/vignette.png", PhotoFilter.VIGNETTE))
        photoFilterList.add(Pair("filters/cross_process.png", PhotoFilter.CROSS_PROCESS))
        photoFilterList.add(Pair("filters/b_n_w.png", PhotoFilter.BLACK_WHITE))
        photoFilterList.add(Pair("filters/flip_horizental.png", PhotoFilter.FLIP_HORIZONTAL))
        photoFilterList.add(Pair("filters/flip_vertical.png", PhotoFilter.FLIP_VERTICAL))
        photoFilterList.add(Pair("filters/rotate.png", PhotoFilter.ROTATE))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(
            ItemEditFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = photoFilterList.size

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(photoFilterList[position])
    }

    inner class FilterViewHolder(val binding: ItemEditFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(photoFilterList[bindingAdapterPosition].second)
            }
        }

        fun bind(pair: Pair<String, PhotoFilter>) {
            binding.apply {
                val fromAsset = getBitmapFromAsset(itemView.context, pair.first)

                imageFilter.setImageBitmap(fromAsset)
                txtFilter.text = pair.second.name.replace("_", " ")
            }
        }
    }
}