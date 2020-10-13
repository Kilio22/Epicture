package com.epitech.epicture.data

import androidx.paging.PagingSource
import com.epitech.epicture.model.Image

class ImgurAccountImagesPagingSource : PagingSource<Int, Image>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        TODO("Not yet implemented")
    }
}