package com.epitech.epicture.ui.image_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epitech.epicture.model.Image
import java.lang.IllegalArgumentException

class ImageDetailsViewModelFactory(private val image: Image, private val commentCountFormat: String) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageDetailsViewModel::class.java)) {
            return ImageDetailsViewModel(image, commentCountFormat) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
