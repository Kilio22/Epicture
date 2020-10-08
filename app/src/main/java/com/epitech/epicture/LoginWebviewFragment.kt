package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.epitech.epicture.config.Config.Companion.CLIENT_ID
import com.epitech.epicture.databinding.FragmentLoginWebviewBinding

class LoginWebviewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginWebviewBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login_webview, container, false
        )
        binding.loginWebview.webViewClient = MyWebViewClient()
        binding.loginWebview.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=" + CLIENT_ID + "&response_type=token")
        return binding.root
    }
}