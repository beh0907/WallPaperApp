package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skymilk.wallpaperapp.databinding.ItemImageBinding
import java.io.File

class MyDownloadImageAdapter(private val images: List<File>) :
    RecyclerView.Adapter<MyDownloadImageAdapter.MyDownloadImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDownloadImageViewHolder {
        return MyDownloadImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyDownloadImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class MyDownloadImageViewHolder(val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(file:File) {
                Glide.with(binding.root).load(file.absolutePath).into(binding.imageDownload)
            }

    }
}