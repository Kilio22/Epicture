package com.epitech.epicture

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.epitech.epicture.databinding.FragmentHomeBinding
import com.epitech.epicture.service.AuthService

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val welcomeMessage = "Welcome, " + AuthService.getCredentialValueByKey("account_username")
        binding.welcomeMessage.text = welcomeMessage
        return binding.root
    }
}