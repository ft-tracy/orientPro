package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.databinding.ActivityLoginBinding
import com.example.orientpro.utils.APIClient
import com.example.orientpro.utils.LoginRequest
import com.example.orientpro.utils.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up click listener for the back button
        binding.btnBack.setOnClickListener {
            finish() // Closes the current activity and returns to the previous one
        }

        // Set up click listener for the login button
        binding.btnLogin.setOnClickListener {
            val emailOrUsername = binding.etEmailOrUsername.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(emailOrUsername, password)) {
                // Attempt login via API
                loginUser(emailOrUsername, password)
            } else {
                showValidationError()
            }
        }

        // Set up click listener for the forgot password text
        binding.tvForgotPassword.setOnClickListener {
            showForgotPasswordMessage()
        }
    }

    private fun validateInput(emailOrUsername: String, password: String): Boolean {
        return emailOrUsername.isNotEmpty() && password.isNotEmpty()
    }

    private fun showValidationError() {
        Toast.makeText(this, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
    }

    private fun showForgotPasswordMessage() {
        Toast.makeText(this, "Forgot password functionality not implemented", Toast.LENGTH_SHORT).show()
    }

    private fun loginUser(emailOrUsername: String, password: String) {
        val loginRequest = LoginRequest(emailOrUsername, password)
        APIClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        if (loginResponse.isFirstLogin) {
                            // Redirect to OTP validation page
                            val intent = Intent(this@LoginActivity, GainAccessActivity::class.java)
                            //intent.putExtra("email", emailOrUsername)
                            startActivity(intent)
                        } else {
                            // Redirect to the dummy home page
                            val intent = Intent(this@LoginActivity, DummyHomePageActivity::class.java)
                            startActivity(intent)
                        }
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}