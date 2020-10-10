package com.epitech.epicture.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.epitech.epicture.R
import com.epitech.epicture.config.Config
import com.epitech.epicture.databinding.FragmentLoginBinding
import com.epitech.epicture.service.AuthService

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginButton.setOnClickListener { this.login() }
        if (AuthService.isLogged) {
            findNavController(this).navigate(R.id.action_loginFragment_to_navigation_home)
        }
        return binding.root
    }

    private fun login() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=" + Config.CLIENT_ID + "&response_type=token")
        )
        startActivity(intent)
    }
}