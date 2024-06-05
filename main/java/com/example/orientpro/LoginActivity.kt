package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.repository.AuthRepository
import com.example.orientpro.utils.Result

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmailOrUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLoginHome: Button
    private lateinit var btnBack: ImageButton

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmailOrUsername = findViewById(R.id.etEmailOrUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLoginHome = findViewById(R.id.btnLoginHome)
        btnBack = findViewById(R.id.btnBack)

        authRepository = AuthRepository(this)

        btnLoginHome.setOnClickListener {
            val email = etEmailOrUsername.text.toString()
            val password = etPassword.text.toString()
            loginUser(email, password)
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loginUser(email: String, password: String) {
        authRepository.login(email, password) { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator if necessary
                }
                is Result.Success -> {
                    // Navigate to HomepageActivity
                    val intent = Intent(this, HomepageActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    // Show error message
                    Toast.makeText(this, "Login failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
