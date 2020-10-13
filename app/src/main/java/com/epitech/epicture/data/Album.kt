package com.epitech.epicture.data

data class Album(
    val id: String,
    val title: String?,
    val description: String?,
    val ups: Int?,
    val downs: Int?,
    val images: List<Image>
)