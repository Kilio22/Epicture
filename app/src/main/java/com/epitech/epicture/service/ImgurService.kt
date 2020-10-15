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

    fun login(context: Context?) {
        if (context != null) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=$CLIENT_ID&response_type=token")
            )
            context.startActivity(intent)
        }
    }

    suspend fun getNewAccessToken(refreshToken: String): ImgurCredentials? {
        return this.retrofitImgurService.getNewAccessToken(
            refreshToken,
            CLIENT_ID,
            Config.CLIENT_SECRET,
            Config.GRANT_TYPE
        )
    }

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

    suspend fun getUserFavorites(
        accessToken: String,
        username: String,
        page: Int,
        sort: String
    ): BasicDataResponse<FavoriteObject> {
        return this.retrofitImgurService.getUserFavorites(
            "Bearer $accessToken",
            username,
            page,
            sort,
        )
    }

    suspend fun getAccountImages(
        accessToken: String,
        page: Int,
    ): BasicDataResponse<Image> {
        return this.retrofitImgurService.getAccountImages("Bearer $accessToken", page)
    }

    suspend fun favImage(
        accessToken: String,
        imageId: String
    ) {
        return this.retrofitImgurService.favImage("Bearer $accessToken", imageId)
    }

    suspend fun favAlbum(
        accessToken: String,
        albumId: String
    ) {
        return this.retrofitImgurService.favAlbum("Bearer $accessToken", albumId)
    }

    suspend fun vote(
        accessToken: String,
        imageId: String,
        vote: String
    ) {
        return this.retrofitImgurService.vote("Bearer $accessToken", imageId, vote)
    }

    suspend fun uploadImage(
        accessToken: String,
        image: MultipartBody.Part,
        title: RequestBody,
        description: RequestBody,
        type: RequestBody
    ) {
        return this.retrofitImgurService.uploadImage(
            "Bearer $accessToken",
            image,
            title,
            description,
            type
        )
    }

    suspend fun getComments(
        accessToken: String,
        id: String,
    ): BasicDataResponse<Comment> {
        return this.retrofitImgurService.getComments("Bearer $accessToken", id)
    }

    suspend fun voteComment(
        accessToken: String,
        commentId: String,
        vote: String
    ) {
        return this.retrofitImgurService.voteComment("Bearer $accessToken", commentId, vote)
    }

    suspend fun createComment(
        accessToken: String,
        imageId: RequestBody,
        comment: RequestBody
    ) {
        return this.retrofitImgurService.createComment(
            "Bearer $accessToken",
            imageId,
            comment
        )
    }
}
