package com.epitech.epicture.model

import com.epitech.epicture.ui.image_details.VoteStatus
import com.google.gson.annotations.SerializedName

/**
 * Comment DAO
 */
data class Comment(
    @SerializedName("id") val id: Int,
    @SerializedName("comment") val comment: String,
    @SerializedName("author") val author: String,
    @SerializedName("ups") var ups: Int,
    @SerializedName("downs") var downs: Int,
    @SerializedName("vote") var vote: String?
) {
    val isUp: Boolean
        get() = this.vote == VoteStatus.UP.value
    val isDown: Boolean
        get() = this.vote == VoteStatus.DOWN.value

    override fun toString(): String {
        return "$id, $comment, $author, $ups, $downs, $vote, $isUp, $isDown"
    }
}