package com.epitech.epicture.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.epitech.epicture.HomeActivity
import com.epitech.epicture.R
import com.epitech.epicture.config.Config
import com.epitech.epicture.databinding.ActivityLoginBinding
import com.epitech.epicture.model.ImgurCredentials
import com.epitech.epicture.service.ImgurService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private val loginScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginButton.setOnClickListener { this.login() }

        this.updateCredentials(this.loadCredentials())
    }

    override fun onResume() {
        super.onResume()

        val imgurCredentials = ImgurService.handleLoginCallback(intent.data)
        if (imgurCredentials != null) {
            intent.data = null
            this.startHomeActivity(imgurCredentials)
        }
    }

    private fun login() {
        ImgurService.login(this)
    }

    private fun updateCredentials(credentials: ImgurCredentials?) {
        if (credentials == null) {
            return
        }
        loginScope.launch {
            try {
                startHomeActivity(ImgurService.getNewAccessToken(credentials.refreshToken))
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    private fun startHomeActivity(newCredentials: ImgurCredentials?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("credentials", newCredentials)
        startActivity(intent)
        finish()
    }

    private fun loadCredentials(): ImgurCredentials? {
        val mPreferences = getSharedPreferences(
            "credentials",
            Context.MODE_PRIVATE
        )

        if (mPreferences != null) {
            val savedCredentialsMap: MutableMap<String, String> =
                mutableMapOf(
                    Config.ACCESS_TOKEN_KEY to (mPreferences.getString(Config.ACCESS_TOKEN_KEY, "")
                        ?: ""),
                    Config.REFRESH_TOKEN_KEY to (mPreferences.getString(
                        Config.REFRESH_TOKEN_KEY,
                        ""
                    ) ?: ""),
                    Config.ACCOUNT_ID_KEY to (mPreferences.getString(Config.ACCOUNT_ID_KEY, "")
                        ?: ""),
                    Config.ACCOUNT_USERNAME_KEY to (mPreferences.getString(
                        Config.ACCOUNT_USERNAME_KEY,
                        ""
                    ) ?: "")
                )
            for (credential in savedCredentialsMap) {
                if (credential.value == "") {
                    return null
                }
            }

            return ImgurCredentials(
                savedCredentialsMap[Config.ACCESS_TOKEN_KEY] ?: return null,
                savedCredentialsMap[Config.REFRESH_TOKEN_KEY] ?: return null,
                savedCredentialsMap[Config.ACCOUNT_USERNAME_KEY] ?: return null,
                savedCredentialsMap[Config.ACCOUNT_ID_KEY] ?: return null
            )
        }
        return null
    }
}