package com.epitech.epicture.data

import androidx.paging.PagingSource
import com.epitech.epicture.HomeActivityData
import com.epitech.epicture.config.Config.Companion.FORMATS_EXTENSION
import com.epitech.epicture.config.Config.Companion.PAGE_INITIAL_IDX
import com.epitech.epicture.model.Image
import com.epitech.epicture.service.ImgurService
import retrofit2.HttpException
import java.io.IOException

class ImgurAccountFavoritesPagingSource : PagingSource<Int, Image>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val position = params.key ?: PAGE_INITIAL_IDX
        return try {
            val favoriteObjects = ImgurService.getUserFavorites(
                HomeActivityData.imgurCredentials?.accessToken ?: "",
                HomeActivityData.imgurCredentials?.accountUsername ?: "",
                position,
                "newest"
            )
            val imageList = mutableListOf<Image>()

            for (favoriteObject in favoriteObjects) {
                if (!FORMATS_EXTENSION.containsKey(favoriteObject.type)) {
                    continue
                }
                if (favoriteObject.isAlbum) {
                    imageList.add(
                        Image(
                            favoriteObject.id,
                            favoriteObject.title,
                            favoriteObject.description,
                            "https://i.imgur.com/" + favoriteObject.cover + FORMATS_EXTENSION[favoriteObject.type],
                            favoriteObject.ups,
                            favoriteObject.downs,
                            favoriteObject.isAlbum,
                            favoriteObject.type
                        )
                    )
                } else {
                    imageList.add(
                        Image(
                            favoriteObject.id,
                            favoriteObject.title,
                            favoriteObject.description,
                            "https://i.imgur.com/" + favoriteObject.id + FORMATS_EXTENSION[favoriteObject.type],
                            favoriteObject.ups,
                            favoriteObject.downs,
                            favoriteObject.isAlbum,
                            favoriteObject.type
                        )
                    )
                }
            }
            LoadResult.Page(
                data = imageList,
                prevKey = if (position == PAGE_INITIAL_IDX) null else position - 1,
                nextKey = if (imageList.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
