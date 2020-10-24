package com.epitech.epicture.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.epitech.epicture.config.Config
import com.epitech.epicture.config.Config.Companion.ACCESS_TOKEN_KEY
import com.epitech.epicture.config.Config.Companion.ACCOUNT_ID_KEY
import com.epitech.epicture.config.Config.Companion.ACCOUNT_USERNAME_KEY
import com.epitech.epicture.config.Config.Companion.CLIENT_ID
import com.epitech.epicture.config.Config.Companion.REFRESH_TOKEN_KEY
import com.epitech.epicture.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * This is the service used to access to imgur data using HTTP requests using Retrofit library
 */
object ImgurService {
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.imgur.com")
        .build()
    private val retrofitImgurService: RetrofitImgurService =
        retrofit.create(
            RetrofitImgurService::
            class.java
        )

    /**
     * Starts new browser activity in order to be logged in
     * @param context Context of the activity
     */
    fun login(context: Context?) {
        if (context != null) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=$CLIENT_ID&response_type=token")
            )
            context.startActivity(intent)
        }
    }

    /**
     * Gets new access token
     * @param refreshToken User refresh token
     */
    suspend fun getNewAccessToken(refreshToken: String): ImgurCredentials? {
        return this.retrofitImgurService.getNewAccessToken(
            refreshToken,
            CLIENT_ID,
            Config.CLIENT_SECRET,
            Config.GRANT_TYPE
        )
    }

    /**
     * Handles login callback
     * @param uri Callback uri containing user infos (access token, refresh token...)
     */
    fun handleLoginCallback(uri: Uri?): ImgurCredentials? {
        if (uri == null) {
            return null
        }
        val uriString = uri.toString()
        if (uriString.startsWith(Config.REDIRECT_URI)) {
            val urlFragment = uri.fragment ?: return null
            val splittedFragment = urlFragment.split('&')
            if (splittedFragment.size < 4) {
                return null
            }

            val credentialsMap = mutableMapOf<String, String>()
            for (param in splittedFragment) {
                val splittedParam = param.split('=')
                if (splittedParam.size != 2) {
                    continue
                }
                credentialsMap[splittedParam[0]] = splittedParam[1]
            }

            return ImgurCredentials(
                credentialsMap[ACCESS_TOKEN_KEY] ?: return null,
                credentialsMap[REFRESH_TOKEN_KEY] ?: return null,
                credentialsMap[ACCOUNT_USERNAME_KEY] ?: return null,
                credentialsMap[ACCOUNT_ID_KEY] ?: return null
            )
        }
        return null
    }

    /**
     * Gets user favorites
     * @param accessToken The user access token
     * @param username The username
     * @param page The page number
     * @param sort The way data is sorted: oldest or newest
     */
    suspend fun getUserFavorites(
        accessToken: String,
        username: String,
        page: Int,
        sort: String
    ): ListDataResponse<GalleryImage> {
        return this.retrofitImgurService.getUserFavorites(
            "Bearer $accessToken",
            username,
            page,
            sort,
        )
    }

    /**
     * Gets uploaded images
     * @param accessToken The user access token
     * @param page The page number
     */
    suspend fun getAccountImages(
        accessToken: String,
        page: Int,
    ): ListDataResponse<Image> {
        return this.retrofitImgurService.getAccountImages("Bearer $accessToken", page)
    }

    /**
     * Favorite or unfavorite an image
     * @param accessToken The user access token
     * @param imageId Id of the image to fav or unfav
     */
    suspend fun favImage(
        accessToken: String,
        imageId: String
    ) {
        return this.retrofitImgurService.favImage("Bearer $accessToken", imageId)
    }

    /**
     * Favorite or unfavorite an album
     * @param accessToken The user access token
     * @param imageId Id of the album to fav or unfav
     */
    suspend fun favAlbum(
        accessToken: String,
        albumId: String
    ) {
        return this.retrofitImgurService.favAlbum("Bearer $accessToken", albumId)
    }

    /**
     * Upvote, downvote or unvote an image / an album
     * @param id The id of the image / the album
     * @param vote The vote type: up, down or veto (unvote)
     */
    suspend fun vote(
        accessToken: String,
        id: String,
        vote: String
    ) {
        return this.retrofitImgurService.vote("Bearer $accessToken", id, vote)
    }

    /**
     * Uploads an image on the imgur platform
     * @param accessToken The user access token
     * @param image The image to upload
     * @param title The title of the image to upload
     * @param description The description of the image to upload
     * @param type The type of the image to upload (jpg, png, gif)
     */
    suspend fun uploadImage(
        accessToken: String,
        image: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        type: RequestBody
    ): BasicDataResponse<Image> {
        return this.retrofitImgurService.uploadImage(
            "Bearer $accessToken",
            image,
            title,
            description,
            type
        )
    }

    /**
     * Shares an image
     * @param accessToken The user access token
     * @param id The id of the image
     * @param title The title of the image
     * @param terms If the user has not accepted Imgur terms, this endpoint will return an error. To by-pass the terms in general simply set this value to 1
     * @param mature If the post is mature, set this value to 1 (https://imgur.com/rules)
     */
    suspend fun shareImage(
        accessToken: String,
        id: String,
        title: RequestBody,
        terms: Int,
        mature: Int
    ) {
        return this.retrofitImgurService.shareImage(
            "Bearer $accessToken",
            id,
            title,
            terms,
            mature
        )
    }

    /**
     * Gets comments of an image / an album
     * @param id The id of the image / album
     */
    suspend fun getComments(
        accessToken: String,
        id: String,
        sort: String
    ): ListDataResponse<Comment> {
        return this.retrofitImgurService.getComments("Bearer $accessToken", id, sort)
    }

    /**
     * Votes for a comment
     * @param accessToken The access token of the user
     * @param commentId The comment to vote
     * @param vote The vote type: up, down or veto (unvote)
     */
    suspend fun voteComment(
        accessToken: String,
        commentId: String,
        vote: String
    ) {
        return this.retrofitImgurService.voteComment("Bearer $accessToken", commentId, vote)
    }

    /**
     * Gets image informations thanks to its id
     * @param imageId The id of the image
     */
    suspend fun getImageById(
        accessToken: String,
        imageId: String
    ): BasicDataResponse<GalleryImage> {
        return this.retrofitImgurService.getImage("Bearer $accessToken", imageId)
    }

    /**
     * Searches images / albums depending on given string
     * @param page The page number
     * @param query The query string
     */
    suspend fun simpleSearch(
        page: Int,
        query: String
    ): ListDataResponse<GalleryImage> {
        return this.retrofitImgurService.simpleSearch("Client-ID $CLIENT_ID", page, query)
    }

    /**
     * Searches images / albums depending on more fields than a simple search
     * @param page The page number
     * @param qAll Search for all of these words (and)
     * @param qAny Search for any of these words (or)
     * @param qExactly Search for exactly this word or phrase
     * @param qType Show results for any file type, jpg | png | gif
     */
    suspend fun advancedSearch(
        page: Int,
        qAll: String,
        qAny: String,
        qExactly: String,
        qType: String,
        sort: String,
        window: String = "all"
    ): ListDataResponse<GalleryImage> {
        return this.retrofitImgurService.advancedSearch(
            "Client-ID $CLIENT_ID",
            sort,
            window,
            page,
            qAll,
            qAny,
            qExactly,
            qType
        )
    }

    /**
     * Gets the avatar of the given user
     * @param accessToken The access token of the user
     * @param username The username of the user
     */
    suspend fun getAvatar(accessToken: String, username: String): BasicDataResponse<Avatar> {
        return this.retrofitImgurService.getAvatar("Bearer $accessToken", username)
    }
}
