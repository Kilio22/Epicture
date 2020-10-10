package com.epitech.epicture.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentUserBinding
import com.epitech.epicture.service.AuthService
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)
        val binding: FragmentUserBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)
        binding.signOutButton.setOnClickListener { signOut() }
        return binding.root
    }

    private fun signOut() {
        AuthService.credentials = mutableMapOf()
        AuthService.isLogged = false
        findNavController(this).navigate(R.id.action_navigation_user_to_login_fragment)
        val mPreferences = activity?.getPreferences(MODE_PRIVATE)
        if (mPreferences != null) {
            val editor = mPreferences.edit()
            editor.clear()
            editor.apply()
        }
        val navView: BottomNavigationView? = activity?.findViewById(R.id.nav_view)
        if (navView != null) {
            navView.visibility = View.GONE
        }
    }
}