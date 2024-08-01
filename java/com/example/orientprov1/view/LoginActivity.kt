package com.example.orientprov1.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.orientprov1.R
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.repository.UserRepository
import com.example.orientprov1.viewmodel.LoginViewModel
import com.example.orientprov1.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLoginHome: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmailOrUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLoginHome = findViewById(R.id.btnLoginHome)
        btnBack = findViewById(R.id.btnBack)

        val apiService = ApiClient.createService(ApiService::class.java)
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val userRepository = UserRepository(apiService, sharedPreferences)
        val viewModelFactory = LoginViewModelFactory(userRepository)
        loginViewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnLoginHome.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            loginViewModel.login(email, password)
            Log.d("LoginActivity", "Login button clicked")
        }

        loginViewModel.loginResult.observe(this, Observer { result ->
            result.onSuccess {
                Log.d("LoginActivity", "Login successful, isFirstLogin: ${it.isFirstLogin}")
                it.token?.let { it1 -> saveToken(it1) }
                if (it.isFirstLogin == true) {
                    startActivity(Intent(this, UpdateUserInfoActivity::class.java))
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("TOKEN_KEY", it.token) // Pass the token
                    startActivity(intent)
                }
                finish()
            }
            result.onFailure {
                Log.d("LoginActivity", "Login failed: ${it.message}")
                Toast.makeText(this, "Login failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        })

        if (loginViewModel.isLoggedIn()) {
            if (loginViewModel.isFirstLogin()) {
                startActivity(Intent(this, UpdateUserInfoActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN_KEY", token)
        editor.apply()
    }
}
