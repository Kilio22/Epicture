package com.epitech.epicture

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.epitech.epicture.config.Config
import com.epitech.epicture.databinding.ActivityHomeBinding
import com.epitech.epicture.model.ImgurCredentials
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val navView: BottomNavigationView = binding.navView

        val credentials = intent.getSerializableExtra("credentials") as? ImgurCredentials?
        HomeActivityData.imgurCredentials = credentials

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
                setOf(
                        R.id.navigation_home,
                        R.id.navigation_favorites,
                        R.id.navigation_search,
                        R.id.navigation_upload,
                        R.id.navigation_user
                )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onPause() {
        super.onPause()

        val homeActivityData = HomeActivityData.imgurCredentials
        val mPreference = getSharedPreferences(
                "credentials",
                Context.MODE_PRIVATE
        ).edit()
        mPreference.putString(Config.ACCESS_TOKEN_KEY, homeActivityData?.accessToken)
        mPreference.putString(Config.REFRESH_TOKEN_KEY, homeActivityData?.refreshToken)
        mPreference.putString(Config.ACCOUNT_USERNAME_KEY, homeActivityData?.accountUsername)
        mPreference.putString(Config.ACCOUNT_ID_KEY, homeActivityData?.accountId)
        mPreference.apply()
    }
}