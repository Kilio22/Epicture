package com.epitech.epicture.service

import com.epitech.epicture.config.Config
import com.epitech.epicture.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Retrofit interface used to access imgur data using Retrofit library
 */
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
        @Header("Authorization") accessToken: String,
        @Path("username") username: String,
        @Path("page") page: Int,
        @Path("favoritesSort") favoritesSort: String,
    ): ListDataResponse<GalleryImage>

    @GET("3/gallery/{imageId}")
    suspend fun getImage(
        @Header("Authorization") accessToken: String,
        @Path("imageId") imageId: String
    ): BasicDataResponse<GalleryImage>

    @POST("3/album/{albumId}/favorite")
    suspend fun favAlbum(
        @Header("Authorization") accessToken: String,
        @Path("albumId") albumId: String
    )

    @POST("3/image/{imageId}/favorite")
    suspend fun favImage(
        @Header("Authorization") accessToken: String,
        @Path("imageId") imageId: String
    )

    @GET("3/account/me/images/{page}")
    suspend fun getAccountImages(
        @Header("Authorization") accessToken: String,
        @Path("page") page: Int,
    ): ListDataResponse<Image>

    @Multipart
    @POST("3/upload")
    suspend fun uploadImage(
        @Header("Authorization") accessToken: String,
        @Part image: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("type") type: RequestBody,
    ): BasicDataResponse<Image>

    @Multipart
    @POST("3/gallery/image/{id}")
    suspend fun shareImage(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String,
        @Part("title") title: RequestBody,
        @Part("terms") terms: Int,
        @Part("mature") mature: Int,
    )

    @POST("3/gallery/{id}/vote/{vote}")
    suspend fun vote(
        @Header("Authorization") accessToken: String,
        @Path("id") id: String,
        @Path("vote") vote: String
    )

    @GET("3/gallery/{imageId}/comments/{commentSort}")
    suspend fun getComments(
        @Header("Authorization") accessToken: String,
        @Path("imageId") imageId: String,
        @Path("commentSort") commentSort: String
    ): ListDataResponse<Comment>

    @POST("3/comment/{commentId}/vote/{vote}")
    suspend fun voteComment(
        @Header("Authorization") accessToken: String,
        @Path("commentId") commentId: String,
        @Path("vote") vote: String
    )

    @GET("3/gallery/search/top/all/{page}")
    suspend fun simpleSearch(
        @Header("Authorization") clientId: String,
        @Path("page") page: Int,
        @Query("q") query: String
    ): ListDataResponse<GalleryImage>

    @GET("3/account/{username}/avatar")
    suspend fun getAvatar(
        @Header("Authorization") accessToken: String,
        @Path("username") username: String
    ): BasicDataResponse<Avatar>

    @GET("3/gallery/search/{sort}/{window}/{page}")
    suspend fun advancedSearch(
        @Header("Authorization") clientId: String,
        @Path("sort") sort: String,
        @Path("window") window: String,
        @Path("page") page: Int,
        @Query("q_all") qAll: String,
        @Query("q_any") qAny: String,
        @Query("q_exactly") qExactly: String,
        @Query("q_type") qType: String
    ): ListDataResponse<GalleryImage>
}