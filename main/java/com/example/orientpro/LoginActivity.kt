package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.databinding.ActivityLoginBinding

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
                // If validation is successful, proceed to the dummy homepage
                val intent = Intent(this, DummyHomePageActivity::class.java)
                startActivity(intent)
            } else {
                // Show error message if validation fails (you can implement this method)
                showValidationError()
            }
        }

        // Set up click listener for the forgot password text
        binding.tvForgotPassword.setOnClickListener {
            // Handle forgot password action
            // You can implement this as needed
            showForgotPasswordMessage()
        }
    }

    private fun validateInput(emailOrUsername: String, password: String): Boolean {
        // Simple validation logic (you can expand this as needed)
        return emailOrUsername.isNotEmpty() && password.isNotEmpty()
    }

    private fun showValidationError() {
        // Show a simple error message (you can customize this as needed)
        android.widget.Toast.makeText(this, "Please fill out all fields correctly", android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun showForgotPasswordMessage() {
        // Show a simple message for forgot password (you can customize this as needed)
        android.widget.Toast.makeText(this, "Forgot password functionality not implemented", android.widget.Toast.LENGTH_SHORT).show()
    }
}
