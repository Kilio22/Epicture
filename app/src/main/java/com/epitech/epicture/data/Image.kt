package com.epitech.epicture.data

data class Image(
    val id: String,
    val title: String?,
    val Description: String?,
    val link: String,
    val ups: Int?,
    val downs: Int?
)