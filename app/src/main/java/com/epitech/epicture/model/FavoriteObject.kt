package com.epitech.epicture.model

import com.google.gson.annotations.SerializedName

data class FavoriteObject(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("is_album") val isAlbum: Boolean,
    @SerializedName("link") val link: String?,
    @SerializedName("ups") val ups: Int?,
    @SerializedName("downs") val downs: Int?,
    @SerializedName("cover") val cover: String?,
    @SerializedName("type") val type: String
)