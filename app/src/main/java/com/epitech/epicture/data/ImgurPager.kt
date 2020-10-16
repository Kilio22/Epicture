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

    fun simpleSearchStream(query: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImgurGallerySimpleSearchPagingSource(query) }
        ).flow
    }

    fun advancedSearchStream(
        qAll: String,
        qAny: String,
        qExactly: String,
        qType: String,
        sort: String
    ): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ImgurGalleryAdvancedSearchPagingSource(
                    qAll,
                    qAny,
                    qExactly,
                    qType,
                    sort
                )
            }
        ).flow
    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}