package com.epitech.epicture.ui.image_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.epitech.epicture.databinding.CommentBinding
import com.epitech.epicture.model.Comment

/**
 * Adapter used to display comments inside the appropriate RecyclerView
 */
class CommentListAdapter(private val clickListener: ClickListener) : RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {

    var commentList: MutableList<Comment>? = null

    class CommentViewHolder(private val binding: CommentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(comment: Comment, clickListener: ClickListener, position: Int) {
            binding.comment = comment
            binding.clickListener = clickListener
            binding.position = position
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

    /**
     * ClickListener interface called when clicking on a comment's upvote or downvote buttons.
     */
    interface ClickListener {
        /**
         * Called when upvote button is clicked
         * @param position Clicked comment's index for the Adapter's comment list
         */
        fun onClickUpvote(position: Int)

        /**
         * Called when downvote button is clicked
         * @param position Clicked comment's index for the Adapter's comment list
         */
        fun onClickDownvote(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = commentList?.get(position)

        item?.let { holder.bind(it, clickListener, position) }
    }

    override fun getItemCount(): Int {
        return commentList?.size ?: 0
    }
}
