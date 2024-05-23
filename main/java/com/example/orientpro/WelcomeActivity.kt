package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listeners
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val isFirstLogin = checkFirstLogin()
            val intent = if (isFirstLogin) {
                Intent(this, GainAccessActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(intent)
        }
    }

    private fun checkFirstLogin(): Boolean {
        val sharedPreferences = getSharedPreferences("com.example.orientpro.PREFERENCES", MODE_PRIVATE)
        val isFirstLogin = sharedPreferences.getBoolean("isFirstLogin", true)

        if (isFirstLogin) {
            // Set the flag to false as it's no longer the first login
            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstLogin", false)
            editor.apply()
        }

        return isFirstLogin
    }


}