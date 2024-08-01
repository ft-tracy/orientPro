package com.example.orientprov1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.UserResponse
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _userDetails = MutableLiveData<UserResponse>()
    val userDetails: LiveData<UserResponse> = _userDetails

    private var _token: String? = null
    val token: String?
        get() = _token

    fun setToken(token: String) {
        _token = token
    }

    private val apiService = ApiClient.createService(ApiService::class.java)

    fun fetchUserDetails() {
        viewModelScope.launch {
            try {
                token?.let { t ->
                    val response = apiService.getUser("Bearer $t")
                    Log.d("MainViewModel", "API Response: $response")
                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            Log.d("MainViewModel", "UserResponse Data: $user")
                            _userDetails.value = UserResponse(
                                documentId = user.documentId,
                                firstName = user.firstName,
                                lastName = user.lastName,
                                email = user.email,
                                role = user.role,
                                hasAccess = user.hasAccess,
                                isFirstLogin = user.isFirstLogin,
                                otp = user.otp,
                                otpExpiration = user.otpExpiration,
                                pwd = user.pwd,
                                enrolledCourses = user.enrolledCourses,
                                courseProgress = user.courseProgress
                            )
                            Log.d("MainViewModel", "Updated UserResponse: ${_userDetails.value}")
                        }
                    } else {
                        Log.e("MainViewModel", "Error response: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Exception: ${e.message}")
            }
        }
    }
}
