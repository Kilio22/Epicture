package com.epitech.epicture.data

import androidx.paging.PagingSource
import com.epitech.epicture.config.Config
import com.epitech.epicture.model.Image
import com.epitech.epicture.service.ImgurService
import retrofit2.HttpException
import java.io.IOException

class ImgurGallerySimpleSearchPagingSource(private val query: String) : PagingSource<Int, Image>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val position = params.key ?: Config.PAGE_INITIAL_IDX
        return try {
            val images =
                ImgurService.simpleSearch(
                    position,
                    query
                ).data
            val imageList = mutableListOf<Image>()

            for (image in images) {
                if (!Config.FORMATS_EXTENSION.containsKey(image.type)) {
                    continue
                }
                val imageLink = if (image.isAlbum) {
                    "https://i.imgur.com/" + image.cover + Config.FORMATS_EXTENSION[image.type]
                } else {
                    "https://i.imgur.com/" + image.id + Config.FORMATS_EXTENSION[image.type]
                }
                imageList.add(
                    Image(
                        image.id,
                        image.title,
                        image.description,
                        imageLink,
                        image.ups,
                        image.downs,
                        image.isAlbum,
                        image.type,
                        image.vote,
                        image.commentCount,
                        image.favoriteCount,
                        image.isFavorite
                    )
                )
            }
            LoadResult.Page(
                data = imageList,
                prevKey = if (position == Config.PAGE_INITIAL_IDX) null else position - 1,
                nextKey = if (imageList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}