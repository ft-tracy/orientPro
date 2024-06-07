package com.example.orientpro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.orientpro.utils.APIClient
import com.example.orientpro.utils.OTPRequest
import com.example.orientpro.utils.OTPResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GainAccessActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnGetAccess: Button
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gain_access)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnGetAccess = findViewById(R.id.btnGetAccess)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        btnGetAccess.setOnClickListener {
            val email = etEmail.text.toString()
            val otp = etPassword.text.toString()
            //validateOtp(email, otp)
        }
    }

    /*private fun validateOtp(email: String, otp: String) {
        val otpRequest = OTPRequest(email, otp)
        APIClient.instance.validateOtp(otpRequest).enqueue(object : Callback<OTPResponse> {
            override fun onResponse(call: Call<OTPResponse>, response: Response<OTPResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(this@GainAccessActivity, "OTP validated", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@GainAccessActivity, UpdateUserInfoActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@GainAccessActivity, "OTP validation failed: ${response.body()?.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OTPResponse>, t: Throwable) {
                Toast.makeText(this@GainAccessActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}