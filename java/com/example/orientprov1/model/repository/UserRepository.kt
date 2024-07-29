package com.example.orientprov1.model.repository

import android.content.SharedPreferences
import com.example.orientprov1.model.LoginRequest
import com.example.orientprov1.model.LoginResponse
import com.example.orientprov1.model.ResetPasswordRequest
import com.example.orientprov1.model.ResetPasswordResponse
import com.example.orientprov1.model.api.ApiService
import retrofit2.Response

class UserRepository(private val apiService: ApiService, private val sharedPreferences: SharedPreferences) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response: Response<LoginResponse> = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    it.token?.let { it1 -> it.isFirstLogin?.let { it2 -> saveUserSession(it1, it2) } }
                    Result.success(it)
                } ?: Result.failure(Exception("Invalid response"))
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String, newPassword: String, confirmPassword: String): Result<String> {
        return try {
            val response: Response<ResetPasswordResponse> = apiService.resetPassword(
                ResetPasswordRequest(email, newPassword, confirmPassword)
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it.message)
                } ?: Result.failure(Exception("Invalid response"))
            } else {
                Result.failure(Exception("Password reset failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun saveUserSession(token: String, isFirstLogin: Boolean) {
        with(sharedPreferences.edit()) {
            putString("TOKEN", token)
            putBoolean("IS_FIRST_LOGIN", isFirstLogin)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getString("TOKEN", null) != null
    }

    fun isFirstLogin(): Boolean {
        return sharedPreferences.getBoolean("IS_FIRST_LOGIN", true)
    }

    // Clear the auth token from SharedPreferences
    fun clearAuthToken() {
        with(sharedPreferences.edit()) {
            remove("TOKEN")
            remove("IS_FIRST_LOGIN")
            apply()
        }
    }


}