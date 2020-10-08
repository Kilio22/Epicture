package com.epitech.epicture

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.navigation.findNavController
import com.epitech.epicture.config.Config.Companion.REDIRECT_URI
import com.epitech.epicture.service.AuthService

class MyWebViewClient : WebViewClient() {
    private val CLIENT_SECRET: String = "800fed47e35ae626a7c1fe0579afbedba4b1449e"

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (view != null && url != null && url.startsWith(REDIRECT_URI)) {
            Toast.makeText(view.context, "Logged in", Toast.LENGTH_SHORT).show()
            val urlFragment = Uri.parse(url).fragment ?: return false
            val splittedFragment = urlFragment.split('&')
            if (splittedFragment.size != 6) {
                return false
            }
            val credentials: MutableMap<String, String> = mutableMapOf()

            for (param in splittedFragment) {
                val splittedParam = param.split('=')
                if (splittedParam.size != 2) {
                    continue
                }
                credentials[splittedParam[0]] = splittedParam[1]
            }
            AuthService.setCredentials(credentials)
            view.findNavController().navigate(R.id.action_loginWebviewFragment_to_homeFragment)
            return true
        }
        return false
    }
}