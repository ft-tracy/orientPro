/*
package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.utils.ApiClient
import com.example.orientpro.utils.UpdateUserInfoRequest
import com.example.orientpro.utils.UpdateUserInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserInfoActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_info)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            updateUserInfo()
        }
    }

    private fun updateUserInfo() {
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

        val updateUserInfoRequest = UpdateUserInfoRequest(email, password, confirmPassword)
        ApiClient.instance.updateUserInfo(updateUserInfoRequest).enqueue(object : Callback<UpdateUserInfoResponse> {
            override fun onResponse(call: Call<UpdateUserInfoResponse>, response: Response<UpdateUserInfoResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@UpdateUserInfoActivity, "User info updated", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@UpdateUserInfoActivity, HomepageActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@UpdateUserInfoActivity, "Update failed: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateUserInfoResponse>, t: Throwable) {
                Toast.makeText(this@UpdateUserInfoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}*/
