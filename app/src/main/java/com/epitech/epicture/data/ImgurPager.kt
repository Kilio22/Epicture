package com.epitech.epicture.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.epitech.epicture.model.Image
import kotlinx.coroutines.flow.Flow

/**
 * Contains methods returning PagingData flows coming from PagingSource implementations.
 */
class ImgurPager {
    /**
     * Gets PagingData favorites flow
     *
     * @param sort The way data must be sorted: oldest or newest
     * @return The flow of PagingData
     */
    fun getAccountFavoritesStream(sort: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImgurAccountFavoritesPagingSource(sort) }
        ).flow
    }

    /**
     * Gets PagingData user uploads flow
     *
     * @return The flow of PagingData
     */
    fun getAccountUploadsStream(): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImgurAccountImagesPagingSource() }
        ).flow
    }

    /**
     * Gets PagingData simple search flow
     *
     * @return The flow of PagingData
     */
    fun simpleSearchStream(query: String): Flow<PagingData<Image>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ImgurGallerySimpleSearchPagingSource(query) }
        ).flow
    }

    /**
     * Gets PagingData advanced search flow
     *
     * @param qAll Search for all of these words
     * @param qAny Search for any of these words
     * @param qExactly Search for exactly this word or phrase
     * @param qType Show results for any file type, jpg | png | gif
     * @param sort The way data must be sorted: time | viral | top
     * @return
     */
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