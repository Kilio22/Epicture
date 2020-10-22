package com.epitech.epicture.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.epitech.epicture.data.ImgurPager
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

/**
 * HomeViewModel contains the observable data used inside HomeFragment
 */
class HomeViewModel : ViewModel() {
    private val _selectedImage = MutableLiveData<Image?>()
    val selectedImage: LiveData<Image?>
        get() = _selectedImage

    private val pager = ImgurPager()

    fun searchUploads(): Flow<PagingData<Image>> {
        return pager.getAccountUploadsStream()
    }

    fun selectImage(image: Image) {
        _selectedImage.value = image
    }

    fun selectImageDone() {
        _selectedImage.value = null
    }
}