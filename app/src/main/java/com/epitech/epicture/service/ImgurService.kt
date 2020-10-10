package com.epitech.epicture.service

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.epitech.epicture.config.Config

object ImgurService {
    var credentials = mutableMapOf<String, String>()
        get() = field
        set(value) {
            field = value
        }
    var isLogged: Boolean = false
        get() = field
        set(value) {
            field = value
        }

    fun getCredentialValueByKey(key: String): String? {
        return this.credentials[key]
    }

    fun login(context: Context?) {
        if (context != null) {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=" + Config.CLIENT_ID + "&response_type=token")
            )
            context.startActivity(intent)
        }
    }

    fun logout() {
        credentials = mutableMapOf()
        isLogged = false
    }

    fun handleLoginCallback(uri: Uri?): Boolean {
        if (uri == null) {
            return false
        }
        if (this.isLogged) {
            return true
        }
        val uriString = uri.toString()
        if (uriString.startsWith(Config.REDIRECT_URI)) {
            val urlFragment = uri.fragment ?: return false
            val splittedFragment = urlFragment.split('&')
            if (splittedFragment.size != 6) {
                return false
            }

            val credentials = mutableMapOf<String, String>()
            for (param in splittedFragment) {
                val splittedParam = param.split('=')
                if (splittedParam.size != 2) {
                    continue
                }
                credentials[splittedParam[0]] = splittedParam[1]
            }
            this.credentials = credentials
            this.isLogged = true
            return true
        }
        return false
    }
}
