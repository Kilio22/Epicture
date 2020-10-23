package com.epitech.epicture.ui.image_details

/**
 * Image vote status
 * @property value String value
 */
enum class VoteStatus(val value: String) {
    /**
     * Image is currently upvoted
     */
    UP("up"),

    /**
     * Image is currently downvoted
     */
    DOWN("down"),

    /**
     * Image is neither upvoted nor downvoted
     */
    VETO("veto")
}
