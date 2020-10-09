package com.epitech.epicture

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.epitech.epicture.config.Config
import com.epitech.epicture.databinding.ActivityMainBinding
import com.epitech.epicture.service.AuthService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.loginButton.setOnClickListener { this.login() }
    }

    private fun login() {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://api.imgur.com/oauth2/authorize?client_id=" + Config.CLIENT_ID + "&response_type=token")
        )
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        if (intent.data != null) {
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
                val homeIntent = Intent(this, HomeActivity::class.java)
                startActivity(homeIntent)
            }
        }
    }
}