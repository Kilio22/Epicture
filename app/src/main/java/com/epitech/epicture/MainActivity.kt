package com.epitech.epicture

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.epitech.epicture.config.Config
import com.epitech.epicture.databinding.ActivityMainBinding
import com.epitech.epicture.service.AuthService
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.login_fragment,
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (!AuthService.isLogged)
            binding.navView.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()

        if (intent.data != null && !AuthService.isLogged) {
            val uri = intent.data
            val uriString = uri.toString()
            if (uri != null && uriString.startsWith(Config.REDIRECT_URI)) {
                Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
                val urlFragment = uri.fragment ?: return
                val splittedFragment = urlFragment.split('&')
                if (splittedFragment.size != 6) {
                    return
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
                AuthService.isLogged = true
                binding.navView.visibility = View.VISIBLE
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_navigation_home)
            }
        }
    }
}