package com.epitech.epicture.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.epitech.epicture.data.ImgurPager
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

/**
 * FavoritesViewModel contains the observable data used inside FavoritesFragment
 */
class FavoritesViewModel : ViewModel() {

    private val pager = ImgurPager()

    private val _selectedImage = MutableLiveData<Image?>()
    val selectedImage: LiveData<Image?>
        get() = _selectedImage

    private val _sort = MutableLiveData<String>()
    val sort: LiveData<String>
        get() = _sort

    fun searchFavorites(sort: String): Flow<PagingData<Image>> {
        return pager.getAccountFavoritesStream(sort)
    }

    fun selectImage(image: Image) {
        _selectedImage.value = image
    }

    fun selectImageDone() {
        _selectedImage.value = null
    }

    fun setSort(newSort: String) {
        this._sort.value = newSort
    }
}