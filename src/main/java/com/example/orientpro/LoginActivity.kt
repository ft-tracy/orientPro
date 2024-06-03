package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.HomepageActivity
import com.example.orientpro.utils.APIClient
import com.example.orientpro.utils.LoginRequest
import com.example.orientpro.utils.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmailOrUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLoginHome: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmailOrUsername = findViewById(R.id.etEmailOrUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLoginHome = findViewById(R.id.btnLoginHome)

        btnLoginHome.setOnClickListener {
            val email = etEmailOrUsername.text.toString()
            val password = etPassword.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)
        APIClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}