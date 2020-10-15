package com.epitech.epicture.model

import com.google.gson.annotations.SerializedName

data class Comment(@SerializedName("id") val id: Int,
                   @SerializedName("comment") val comment: String,
                   @SerializedName("ups") val ups: Int?,
                   @SerializedName("downs") val downs: Int?
)