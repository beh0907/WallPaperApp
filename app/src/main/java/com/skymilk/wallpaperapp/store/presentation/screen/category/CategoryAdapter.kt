package com.skymilk.wallpaperapp.store.presentation.screen.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skymilk.wallpaperapp.R
import com.skymilk.wallpaperapp.databinding.ItemCategoryBinding
import com.skymilk.wallpaperapp.store.domain.model.Category
import com.skymilk.wallpaperapp.util.ImageUtil

class CategoryAdapter(
    private val categoryList: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    var onItemClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(categoryList[bindingAdapterPosition].categoryName)
            }
        }

        fun bind(category: Category) {
            binding.apply {
                txtCategory.text = category.categoryName

                Glide.with(itemView.context)
                    .load(category.imageUrl)
                    .placeholder(ImageUtil.getShimmerDrawable())
                    .optionalCenterCrop()
                    .error(R.color.teal_200)
                    .into(imageCategory)
            }
        }
    }
}