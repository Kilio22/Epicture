package com.epitech.epicture.ui.image_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.ObjectsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.epitech.epicture.databinding.CommentBinding
import com.epitech.epicture.model.Comment

class CommentGridAdapter : ListAdapter<Comment, CommentGridAdapter.CommentViewHolder>(CommentDiffCallback) {

    class CommentViewHolder(private val binding: CommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment) {
            binding.comment = comment
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CommentViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CommentBinding.inflate(layoutInflater, parent, false)

                return CommentViewHolder(binding)
            }
        }
    }

    companion object CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return ObjectsCompat.equals(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = getItem(position)

        item?.let { holder.bind(it) }
    }
}
