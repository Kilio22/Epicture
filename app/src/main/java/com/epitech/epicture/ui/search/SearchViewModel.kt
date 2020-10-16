package com.epitech.epicture.ui.search

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.epitech.epicture.data.ImgurPager
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

class SearchViewModel : ViewModel() {
    private val pager = ImgurPager()

    fun simpleSearch(query: String): Flow<PagingData<Image>> {
        return pager.simpleSearchStream(query)
    }
}