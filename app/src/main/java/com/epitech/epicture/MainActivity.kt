package com.epitech.epicture

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.epitech.epicture.config.Config.Companion.ACCESS_TOKEN_KEY
import com.epitech.epicture.config.Config.Companion.ACCOUNT_ID_KEY
import com.epitech.epicture.config.Config.Companion.ACCOUNT_USERNAME_KEY
import com.epitech.epicture.config.Config.Companion.REFRESH_TOKEN_KEY
import com.epitech.epicture.databinding.ActivityMainBinding
import com.epitech.epicture.service.ImgurService
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.login_fragment,
                R.id.navigation_home,
                R.id.navigation_favorites,
                R.id.navigation_search,
                R.id.navigation_upload,
                R.id.navigation_user
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        this.loadCredentials()
        if (!ImgurService.isLogged)
            binding.navView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        if (ImgurService.handleLoginCallback(intent.data)) {
            binding.navView.visibility = View.VISIBLE
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_navigation_home)
            intent.data = null
        }
    }

    override fun onPause() {
        super.onPause()

        val credentials = ImgurService.credentials
        val mPreference = getPreferences(MODE_PRIVATE).edit()
        for (credential in credentials) {
            mPreference.putString(credential.key, credential.value)
        }
        mPreference.apply()
    }

    private fun loadCredentials() {
        val mPreferences = getPreferences(MODE_PRIVATE)

        if (mPreferences != null) {
            val savedCredentials: MutableMap<String, String> =
                mutableMapOf(
                    ACCESS_TOKEN_KEY to (mPreferences.getString(ACCESS_TOKEN_KEY, "") ?: ""),
                    REFRESH_TOKEN_KEY to (mPreferences.getString(REFRESH_TOKEN_KEY, "") ?: ""),
                    ACCOUNT_ID_KEY to (mPreferences.getString(ACCOUNT_ID_KEY, "") ?: ""),
                    ACCOUNT_USERNAME_KEY to (mPreferences.getString(ACCOUNT_USERNAME_KEY, "") ?: "")
                )

            for (credential in savedCredentials) {
                if (credential.value == "") {
                    binding.navView.visibility = View.GONE
                    return
                }
            }
            ImgurService.credentials = savedCredentials
            ImgurService.isLogged = true
            return
        }
        return
    }
}