package com.epitech.epicture.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.epitech.epicture.data.ImgurPager
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

class FavoritesViewModel : ViewModel() {
    private val pager = ImgurPager()

    fun searchFavorites(): Flow<PagingData<Image>> {
        return pager.getAccountFavoritesStream()
    }
}