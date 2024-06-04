package com.example.orientpro

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.utils.APIService

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmailOrUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLoginHome: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmailOrUsername = findViewById(R.id.etEmailOrUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLoginHome = findViewById(R.id.btnLoginHome)
        btnBack = findViewById(R.id.btnBack)

        btnLoginHome.setOnClickListener {
            val email = etEmailOrUsername.text.toString()
            val password = etPassword.text.toString()
            APIService.login(this, email, password)  // Call the login method from APIService
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}
