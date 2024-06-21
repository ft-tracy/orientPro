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

class SignUpActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignupHome: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignupHome = findViewById(R.id.btnSignupHome)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSignupHome.setOnClickListener {
            handleSignUp()
        }
    }

    private fun handleSignUp() {
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        when {
            firstName.isEmpty() -> {
                showToast("Please fill out your first name")
                Log.e("SignUpActivity", "First name field is empty")
            }
            lastName.isEmpty() -> {
                showToast("Please fill out your last name")
                Log.e("SignUpActivity", "Last name field is empty")
            }
            email.isEmpty() -> {
                showToast("Please fill out your email")
                Log.e("SignUpActivity", "Email field is empty")
            }
            password.isEmpty() -> {
                showToast("Please fill out your password")
                Log.e("SignUpActivity", "Password field is empty")
            }
            confirmPassword.isEmpty() -> {
                showToast("Please fill out your confirm password")
                Log.e("SignUpActivity", "Confirm password field is empty")
            }
            password != confirmPassword -> {
                showToast("Passwords do not match")
                Log.e("SignUpActivity", "Passwords do not match")
            }
            else -> {
                performSignUp(firstName, lastName, email, password, confirmPassword)
            }
        }
    }

    private fun performSignUp(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
        val signUpRequest = SignUpRequest(firstName, lastName, email, password, confirmPassword)
        val apiService = ApiClient.createService(ApiService::class.java)

        apiService.signUp(signUpRequest).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful && response.body()?.message == "Sign-up successful.") {
                    // Handle successful sign-up
                    Log.d("SignUpActivity", "Sign-up successful for email: $email")
                    showToast("Sign-up successful")
                    val intent = Intent(this@SignUpActivity, HomepageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.body()?.message ?: "Sign-up failed"
                    Log.e("SignUpActivity", "Sign-up failed for email: $email - $errorMessage")
                    showToast(errorMessage)
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("SignUpActivity", "Sign-up failed: ${t.message}")
                showToast("Sign-up failed: ${t.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
