package com.epitech.epicture.ui.image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ImageDetailsViewModelFactory is used to create ImageDetailsViewModel using [imageId] and [commentCountFormat] args
 */
class ImageDetailsViewModelFactory(private val imageId: String, private val commentCountFormat: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageDetailsViewModel::class.java)) {
            return ImageDetailsViewModel(imageId, commentCountFormat) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
