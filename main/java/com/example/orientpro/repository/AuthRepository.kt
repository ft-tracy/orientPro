package com.example.orientpro.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.orientpro.utils.APIClient
import com.example.orientpro.utils.LoginRequest
import com.example.orientpro.utils.LoginResponse
import com.example.orientpro.utils.SignUpRequest
import com.example.orientpro.utils.SignUpResponse
import com.example.orientpro.utils.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    fun login(email: String, password: String, callback: (Result<LoginResponse>) -> Unit) {
        val loginRequest = LoginRequest(email, password)
        callback(Result.Loading)
        APIClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.success) {
                        saveToken(loginResponse.token)
                        callback(Result.Success(loginResponse))
                    } else {
                        val message = loginResponse?.message ?: "Unknown error"
                        callback(Result.Error(Exception(message)))
                    }
                } else {
                    callback(Result.Error(Exception(response.message())))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback(Result.Error(t))
            }
        })
    }

    fun signUp(signUpRequest: SignUpRequest, callback: (Result<SignUpResponse>) -> Unit) {
        callback(Result.Loading)
        APIClient.instance.signUp(signUpRequest).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null) {
                        callback(Result.Success(signUpResponse))
                    } else {
                        val message = "Unknown error"
                        callback(Result.Error(Exception(message)))
                    }
                } else {
                    callback(Result.Error(Exception(response.message())))
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(Result.Error(t))
            }
        })
    }

    private fun saveToken(token: String?) {
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("token", null)
    }
}
