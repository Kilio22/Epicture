package com.epitech.epicture.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.epitech.epicture.HomeActivity
import com.epitech.epicture.R
import com.epitech.epicture.config.Config
import com.epitech.epicture.databinding.ActivityLoginBinding
import com.epitech.epicture.model.ImgurCredentials
import com.epitech.epicture.service.ImgurService
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Login activity
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    /**
     * Creates activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = LoginViewModel()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.loginButton.setOnClickListener { this.login() }
        binding.viewModel = loginViewModel
        binding.lifecycleOwner = this
        this.updateCredentials(this.loadCredentials())
    }

    /**
     * Called when application is re-opened after stating web view to connect using OAUTH2
     * It handles callback uri
     */
    override fun onResume() {
        super.onResume()

        val imgurCredentials = ImgurService.handleLoginCallback(intent.data)
        if (imgurCredentials != null) {
            intent.data = null
            this.startHomeActivity(imgurCredentials)
        }
    }

    /**
     * Starts a web intent to connect using OAUTH2
     */
    private fun login() {
        ImgurService.login(this)
    }

    /**
     * Check if restored credentials are valid
     */
    private fun updateCredentials(credentials: ImgurCredentials?) {
        if (credentials == null) {
            loginViewModel.setStatus(LoginViewModel.LoginStatus.MUST_LOGIN)
            return
        }
        loginViewModel.setStatus(LoginViewModel.LoginStatus.LOADING)
        lifecycleScope.launch {
            try {
                val newCredentials = ImgurService.getNewAccessToken(credentials.refreshToken)
                startHomeActivity(newCredentials)
            } catch (e: Exception) {
                Timber.tag("Login activity").e(e.toString())
                loginViewModel.setStatusAsync(LoginViewModel.LoginStatus.MUST_LOGIN)
            }
        }
    }

    /**
     * Starts home activity
     */
    private fun startHomeActivity(newCredentials: ImgurCredentials?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("credentials", newCredentials)
        startActivity(intent)
        finish()
    }

    /**
     * Loads saved credentials
     */
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