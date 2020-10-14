package com.epitech.epicture.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

class ImgurPager {
    fun getAccountFavoritesStream(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImgurAccountFavoritesPagingSource() }
        ).flow
    }

    fun getAccountUploadsStream(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImgurAccountImagesPagingSource() }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}