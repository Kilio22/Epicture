package com.epitech.epicture.ui.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.epitech.epicture.HomeActivityData
import com.epitech.epicture.R
import com.epitech.epicture.databinding.FragmentUserBinding
import com.epitech.epicture.service.ImgurService
import com.epitech.epicture.ui.login.LoginActivity
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * User fragment
 */
class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    /**
     * Creates fragment
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUserBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)

        userViewModel =
                ViewModelProvider(this).get(UserViewModel::class.java)

        binding.signOutButton.setOnClickListener { signOut() }
        binding.viewModel = userViewModel
        binding.lifecycleOwner = this
        userViewModel.setUsername(HomeActivityData.imgurCredentials!!.accountUsername)
        getAvatar()
        return binding.root
    }

    /**
     * Signs out user
     */
    private fun signOut() {
        this.clearSharedPreferences()

        HomeActivityData.imgurCredentials = null
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Clears shared preferences
     */
    private fun clearSharedPreferences() {
        val mPreferences = activity?.getPreferences(MODE_PRIVATE)

        if (mPreferences != null) {
            val editor = mPreferences.edit()
            editor.clear()
            editor.apply()
        }
    }

    /**
     * Gets user avatar
     */
    private fun getAvatar() {
        lifecycleScope.launch {
            try {
                val response = ImgurService.getAvatar(
                        HomeActivityData.imgurCredentials!!.accessToken,
                        HomeActivityData.imgurCredentials!!.accountUsername
                )
                userViewModel.setAvatarUrl(response.data.avatar ?: "")
            } catch (e: Exception) {
                Timber.tag("User fragment").e(e.toString())
            }
        }
    }
}