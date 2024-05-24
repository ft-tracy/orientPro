package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.databinding.ActivityGainAccessBinding

class GainAccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGainAccessBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGainAccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)

        // Set up click listener for the back button
        binding.btnBack.setOnClickListener {
            finish() // Closes the current activity and returns to the previous one
        }

        // Set up click listener for the Get Access button
        binding.btnGetAccess.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                if (databaseHelper.validateUser(email, password)) {
                    // If validation is successful, proceed to UpdateUserInfoActivity
                    val intent = Intent(this, UpdateUserInfoActivity::class.java)
                    startActivity(intent)
                } else {
                    // Show error message if validation fails
                    showValidationError()
                }
            } else {
                // Show error message if input validation fails
                showValidationError()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        // Simple input validation logic
        return email.isNotEmpty() && password.isNotEmpty()
    }

    private fun showValidationError() {
        // Show a simple error message
        android.widget.Toast.makeText(this, "Invalid email or password", android.widget.Toast.LENGTH_SHORT).show()
    }
}
