package com.epitech.epicture.ui.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.epitech.epicture.data.ImgurPager
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {
    private val pager = ImgurPager()

    fun searchUploads(): Flow<PagingData<Image>> {
        return pager.getAccountUploadsStream()
    }
}