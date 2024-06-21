package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmailOrUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLoginHome)
        btnBack = findViewById(R.id.btnBack)

        btnLogin.setOnClickListener {
            handleLogin()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun handleLogin() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        when {
            email.isEmpty() -> {
                showToast("Please fill out your email")
                Log.e("LoginActivity", "Email field is empty")
            }
            password.isEmpty() -> {
                showToast("Please fill out your password")
                Log.e("LoginActivity", "Password field is empty")
            }
            else -> {
                performLogin(email, password)
            }
        }
    }

    private fun performLogin(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        val apiService = ApiClient.createService(ApiService::class.java)



        apiService.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.message == "Login successful.") {
                    // Handle successful login
                    Log.d("LoginActivity", "Login successful for email: $email")
                    showToast("Login successful")
                    val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.body()?.message ?: "Login failed"
                    Log.e("LoginActivity", "Login failed for email: $email - $errorMessage")
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Login failed: ${t.message}")
                showToast("Login failed: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
