package com.epitech.epicture

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.epitech.epicture.databinding.ActivityMainBinding
import com.epitech.epicture.ui.login.LoginActivity

/**
 * Main activity
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * Creates activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}