package com.epitech.epicture

import com.epitech.epicture.model.ImgurCredentials

object HomeActivityData {
    var imgurCredentials: ImgurCredentials? = null
        get() = field
        set(value) {
            field = value
        }
}