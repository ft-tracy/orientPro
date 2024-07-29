package com.example.orientprov1.viewmodel

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

    private var token: String? = null
    private val apiService = ApiClient.createService(ApiService::class.java)

    fun setToken(token: String) {
        this.token = token
    }

    fun getToken(): String? {
        return token
    }

    fun fetchUserDetails() {
        viewModelScope.launch {
            try {
                token?.let { t ->
                    val response = apiService.getUser("Bearer $t")
                    if (response.isSuccessful) {
                        response.body()?.let { user ->
                            _userDetails.value = UserResponse(
                                userId = user.userId,
                                firstName = user.firstName,
                                lastName = user.lastName,
                                email = user.email,
                                companyRole = user.companyRole
                            )
                        }
                    } else {
                        // Handle error
                    }
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
}
