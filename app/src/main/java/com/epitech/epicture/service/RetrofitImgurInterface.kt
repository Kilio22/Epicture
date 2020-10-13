package com.epitech.epicture.service

import com.epitech.epicture.config.Config
import com.epitech.epicture.data.ImgurCredentials
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitImgurService {
    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getNewAccessToken(
        @Field(Config.REFRESH_TOKEN_KEY) refreshToken: String,
        @Field(Config.CLIENT_ID_KEY) clientId: String,
        @Field(Config.CLIENT_SECRET_KEY) clientSecret: String,
        @Field(Config.GRANT_TYPE_KEY) grantType: String
    ): ImgurCredentials
}