package com.skymilk.wallpaperapp.store.presentation.screen.download

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.skymilk.wallpaperapp.databinding.ItemMyDownloadImageBinding
import java.io.File

class MyDownloadImageAdapter :
    RecyclerView.Adapter<MyDownloadImageAdapter.MyDownloadImageViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.absolutePath == newItem.absolutePath
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.lastModified() == newItem.lastModified()
        }
    }
    val differ = AsyncListDiffer(this, diffCallback)

    fun removeItem(position: Int) {
        val currentList = differ.currentList.toMutableList()
        currentList.removeAt(position)
        differ.submitList(currentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDownloadImageViewHolder {
        return MyDownloadImageViewHolder(
            ItemMyDownloadImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyDownloadImageViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class MyDownloadImageViewHolder(val binding: ItemMyDownloadImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File) {
            Glide.with(binding.root)
                .load(file.absolutePath)
                .signature(ObjectKey(file.lastModified().toString()))
                .optionalCenterCrop()
                .into(binding.imageDownload)
        }

    }
}