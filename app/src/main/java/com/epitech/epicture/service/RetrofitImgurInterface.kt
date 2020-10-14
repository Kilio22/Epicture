package com.epitech.epicture.service

import com.epitech.epicture.config.Config
import com.epitech.epicture.model.BasicDataResponse
import com.epitech.epicture.model.FavoriteObject
import com.epitech.epicture.model.Image
import com.epitech.epicture.model.ImgurCredentials
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface RetrofitImgurService {
    @FormUrlEncoded
    @POST("/oauth2/token")
    suspend fun getNewAccessToken(
        @Field(Config.REFRESH_TOKEN_KEY) refreshToken: String,
        @Field(Config.CLIENT_ID_KEY) clientId: String,
        @Field(Config.CLIENT_SECRET_KEY) clientSecret: String,
        @Field(Config.GRANT_TYPE_KEY) grantType: String
    ): ImgurCredentials

    @GET("3/account/{username}/favorites/{page}/{favoritesSort}")
    suspend fun getUserFavorites(
        @Path("username") username: String,
        @Path("page") page: Int,
        @Path("favoritesSort") favoritesSort: String,
        @Header("Authorization") accessToken: String
    ): BasicDataResponse<FavoriteObject>

    @POST("3/album/{albumId}/favorite")
    suspend fun favAlbum(
        @Path("albumId") albumId: String,
        @Header("Authorization") accessToken: String
    )

    @POST("3/image/{imageId}/favorite")
    suspend fun favImage(
        @Path("imageId") imageId: String,
        @Header("Authorization") accessToken: String
    )

    @GET("3/account/me/images/{page}")
    suspend fun getAccountImages(
        @Header("Authorization") accessToken: String,
        @Path("page") page: Int,
    ): BasicDataResponse<Image>

    @Multipart
    @POST("3/upload")
    suspend fun uploadImage(
        @Header("Authorization") accessToken: String,
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("type") type: RequestBody,
    )
}