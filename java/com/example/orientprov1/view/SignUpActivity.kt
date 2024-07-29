package com.example.orientprov1.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.orientprov1.R
import com.example.orientprov1.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignupHome: Button
    private lateinit var btnBack: ImageButton

    private val signUpViewModel: SignUpViewModel by viewModels()

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
            onBackPressedDispatcher.onBackPressed()
        }

        btnSignupHome.setOnClickListener {
            handleSignUp()
        }

        signUpViewModel.signUpResult.observe(this) { result ->
            result.onSuccess { response ->
                if (response.message == "Sign-up successful.") {
                    Log.d("SignUpActivity", "Sign-up successful: ${response.message}")
                    showToast("Sign-up successful")
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.message
                    Log.e("SignUpActivity", "Sign-up failed: $errorMessage")
                    showToast(errorMessage)
                }
            }.onFailure { t ->
                Log.e("SignUpActivity", "Sign-up failed: ${t.message}")
                showToast("Sign-up failed: ${t.message}")
            }
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
                signUpViewModel.signUp(firstName, lastName, email, password, confirmPassword)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
