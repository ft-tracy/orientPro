package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateUserInfoActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnProceed: Button
    private lateinit var btnBack: ImageButton
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)

        // Initialize the views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnProceed = findViewById(R.id.btnProceed)
        btnBack = findViewById(R.id.btnBack)

        // Initialize the database helper
        dbHelper = DatabaseHelper(this)

        // Handle the back button click
        btnBack.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Handle the proceed button click
        btnProceed.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confirmPassword = etConfirmPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val newRowId = dbHelper.insertUser(email, password)
        if (newRowId == -1L) {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
            // Redirect to the dummy home page
            val intent = Intent(this, DummyHomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
