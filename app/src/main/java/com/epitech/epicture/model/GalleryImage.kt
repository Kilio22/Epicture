package com.epitech.epicture.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * DAO representing an image / an album published onto Imgur's gallery feed
 */
@Parcelize
data class GalleryImage(
        @SerializedName("id") val id: String,
        @SerializedName("title") val title: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("is_album") val isAlbum: Boolean,
        @SerializedName("link") val link: String,
        @SerializedName("ups") val ups: Int,
        @SerializedName("downs") val downs: Int,
        @SerializedName("cover") val cover: String?,
        @SerializedName("type") val type: String?,
        @SerializedName("vote") val vote: String?,
        @SerializedName("comment_count") val commentCount: Int,
        @SerializedName("favorite_count") val favoriteCount: Int,
        @SerializedName("favorite") val isFavorite: Boolean,
        @SerializedName("images") val images: List<Image>?,
        @SerializedName("in_gallery") val inGallery: Boolean
) : Parcelable
