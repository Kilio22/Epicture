package com.epitech.epicture.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImgurCredentials(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("account_username")
    val accountUsername: String,

    @SerializedName("account_id")
    val accountId: String
) : Serializable