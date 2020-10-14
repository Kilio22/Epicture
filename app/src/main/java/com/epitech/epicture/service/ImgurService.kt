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
import com.epitech.epicture.model.BasicDataResponse
import com.epitech.epicture.model.FavoriteObject
import com.epitech.epicture.model.Image
import com.epitech.epicture.model.ImgurCredentials
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
            username,
            page,
            sort,
            "Bearer $accessToken"
        )
    }

    suspend fun getAccountImages(
        accessToken: String,
    ): BasicDataResponse<Image> {
        return this.retrofitImgurService.getAccountImages("Bearer $accessToken")
    }

    suspend fun favImage(
        accessToken: String,
        imageId: String
    ) {
        return this.retrofitImgurService.favImage(imageId, "Bearer $accessToken")
    }

    suspend fun favAlbum(
        accessToken: String,
        albumId: String
    ) {
        return this.retrofitImgurService.favImage(albumId, "Bearer $accessToken")
    }
}
