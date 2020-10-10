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
import com.epitech.epicture.config.Config.Companion.ACCOUNT_USERNAME_KEY
import com.epitech.epicture.databinding.FragmentUserBinding
import com.epitech.epicture.service.ImgurService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_user.*

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
        userViewModel.username.observe(viewLifecycleOwner, {
            this.username_text_view.text = it
        })
        binding.signOutButton.setOnClickListener { signOut() }
        binding.usernameTextView.text = ImgurService.getCredentialValueByKey(ACCOUNT_USERNAME_KEY)
        return binding.root
    }

    private fun signOut() {
        ImgurService.logout()
        this.clearSharedPreferences()

        findNavController(this).navigate(R.id.action_navigation_user_to_login_fragment)
        val navView: BottomNavigationView? = activity?.findViewById(R.id.nav_view)
        if (navView != null) {
            navView.visibility = View.GONE
        }
    }

    private fun clearSharedPreferences() {
        val mPreferences = activity?.getPreferences(MODE_PRIVATE)

        if (mPreferences != null) {
            val editor = mPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }
}