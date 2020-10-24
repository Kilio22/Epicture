package com.epitech.epicture.data

import androidx.paging.PagingSource
import com.epitech.epicture.config.Config
import com.epitech.epicture.config.Config.Companion.FORMATS_EXTENSION
import com.epitech.epicture.model.GalleryImage
import com.epitech.epicture.model.Image
import com.epitech.epicture.service.ImgurService
import okhttp3.internal.toImmutableList
import retrofit2.HttpException
import java.io.IOException

/**
 * Returns a PagingSource implementation, used when searching images using multiple query parameters
 */
internal class ImgurGalleryAdvancedSearchPagingSource(
    private val qAll: String,
    private val qAny: String,
    private val qExactly: String,
    private val qType: String,
    private val sort: String
) : PagingSource<Int, Image>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        val position = params.key ?: Config.PAGE_INITIAL_IDX
        return try {
            val imgurImages =
                ImgurService.advancedSearch(
                    position,
                    qAll,
                    qAny,
                    qExactly,
                    if (qType == "all") "" else qType,
                    sort
                ).data
            val imageList = this.getImageList(imgurImages, qType)

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

    /**
     * Extracts images from fetched data
     *
     * @param imgurImages Fetched data
     * @return The image list
     */
    private fun getImageList(imgurImages: List<GalleryImage>, type: String): List<Image> {
        val imageList = mutableListOf<Image>()

        for (imgurImage in imgurImages) {
            val imageLink =
                if ((imgurImage.type != null && FORMATS_EXTENSION.containsKey(imgurImage.type))) {
                    if (imgurImage.isAlbum) {
                        "https://i.imgur.com/" + imgurImage.cover + FORMATS_EXTENSION[imgurImage.type]
                    } else {
                        "https://i.imgur.com/" + imgurImage.id + FORMATS_EXTENSION[imgurImage.type]
                    }
                } else if (type != "all") {
                    if (imgurImage.isAlbum) {
                        "https://i.imgur.com/" + imgurImage.cover + ".$type"
                    } else {
                        "https://i.imgur.com/" + imgurImage.id + ".$type"
                    }
                } else {
                    continue
                }
            imageList.add(
                Image(
                    imgurImage.id,
                    imgurImage.title,
                    imgurImage.description,
                    imageLink,
                    imgurImage.ups,
                    imgurImage.downs,
                    imgurImage.isAlbum,
                    imgurImage.type,
                    imgurImage.vote,
                    imgurImage.commentCount,
                    imgurImage.favoriteCount,
                    imgurImage.isFavorite
                )
            )
        }
        return imageList.toImmutableList()
    }
}
