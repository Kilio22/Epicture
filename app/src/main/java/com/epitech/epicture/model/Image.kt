package com.epitech.epicture.model

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("ups") val ups: Int,
    @SerializedName("downs") val downs: Int,
    @SerializedName("is_album") val isAlbum: Boolean,
    @SerializedName("type") val type: String,
    @SerializedName("vote") val vote: String?,
    @SerializedName("comment_count") val commentCount: Int,
    @SerializedName("favorite_count") val favoriteCount: Int,
    @SerializedName("favorite") val isFavorite: Boolean
)