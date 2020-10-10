package com.epitech.epicture.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentLoginBinding
import com.epitech.epicture.service.ImgurService

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginButton.setOnClickListener { this.login() }
        if (ImgurService.isLogged) {
            findNavController(this).navigate(R.id.action_loginFragment_to_navigation_home)
        }
        return binding.root
    }

    private fun login() {
        ImgurService.login(context)
    }
}