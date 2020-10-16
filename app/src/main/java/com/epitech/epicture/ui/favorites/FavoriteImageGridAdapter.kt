package com.epitech.epicture.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.epitech.epicture.databinding.ImageFavoriteBinding
import com.epitech.epicture.model.Image

//enum class ImageType { UPLOAD, FAVORITE }

class FavoriteImageGridAdapter(private val clickListener: ClickListener) :
    PagingDataAdapter<Image, FavoriteImageGridAdapter.FavoriteImageViewHolder>(PhotoDiffCallback) {
    class FavoriteImageViewHolder(private val binding: ImageFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image, clickListener: ClickListener) {
            binding.innerImage.image = image
            binding.innerImage.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavoriteImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ImageFavoriteBinding.inflate(layoutInflater, parent, false)

                return FavoriteImageViewHolder(binding)
            }
        }
    }

    class ClickListener(val clickListener: (image: Image) -> Unit) {
        fun onClick(image: Image) = clickListener(image)
    }

    companion object PhotoDiffCallback : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteImageViewHolder {
        return FavoriteImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteImageViewHolder, position: Int) {
        val item = getItem(position)

        item?.let { holder.bind(it, clickListener) }
    }
}
