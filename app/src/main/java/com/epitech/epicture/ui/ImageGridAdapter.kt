package com.epitech.epicture.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.epitech.epicture.databinding.ImageBinding
import com.epitech.epicture.model.Image

/**
 * RecyclerView adapter used to work with paged data, it takes a [clickListener] as parameter used to handle clicks on an image
 */
class ImageGridAdapter(private val clickListener: ClickListener) : PagingDataAdapter<Image, ImageGridAdapter.ImageViewHolder>(PhotoDiffCallback) {

    class ImageViewHolder(private val binding: ImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Image, clickListener: ClickListener) {
            binding.image = image
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ImageViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ImageBinding.inflate(layoutInflater, parent, false)

                return ImageViewHolder(binding)
            }
        }
    }

    /**
     * ClickListener class called when clicking on an image
     */
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = getItem(position)

        item?.let { holder.bind(it, clickListener) }
    }
}
