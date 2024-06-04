package com.example.orientpro.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.orientpro.HomepageActivity
import com.example.orientpro.SignUpActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val success: Boolean, val message: String, val token: String?, val isFirstLogin: Boolean)
data class OTPRequest(val email: String, val otp: String)
data class OTPResponse(val success: Boolean, val message: String)
data class UpdateUserInfoRequest(val email: String, val password: String, val confirmPassword: String)
data class UpdateUserInfoResponse(val success: Boolean, val message: String)
data class SignUpRequest(val email: String, val password: String, val confirmPassword: String)
data class SignUpResponse(val success: Boolean, val message: String)

interface APIService {

    @POST("account/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("validateOtp")
    fun validateOtp(@Body request: OTPRequest): Call<OTPResponse>

    @POST("updateUserInfo")
    fun updateUserInfo(@Body request: UpdateUserInfoRequest): Call<UpdateUserInfoResponse>

    @POST("account/signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>

    companion object {
        fun login(context: Context, email: String, password: String) {
            val loginRequest = LoginRequest(email, password)
            APIClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.success) {
                            Log.i("APIService", "Login successful")
                            Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, HomepageActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            context.startActivity(intent)
                        } else {
                            val message = loginResponse?.message ?: "Unknown error"
                            Log.e("APIService", "Login failed: $message")
                            Log.e("APIService", "Login failed: ${response.code()}")
                            Toast.makeText(context, "Login failed: $message", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("APIService", "Login failed: ${response.message()}")
                        Log.e("APIService", "Login failed: ${response.code()}")
                        Toast.makeText(context, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.e("APIService", "Error: ${t.message}", t)
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
