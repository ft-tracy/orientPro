package com.example.orientprov1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.UserResponse
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    val userName: MutableLiveData<String> = MutableLiveData()
    val firstName: MutableLiveData<String> = MutableLiveData()
    val lastName: MutableLiveData<String> = MutableLiveData()
    val email: MutableLiveData<String> = MutableLiveData()
    val companyRole: MutableLiveData<String> = MutableLiveData()

    private val apiService = ApiClient.createService(ApiService::class.java)

    fun fetchData(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUser("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        firstName.value = user.firstName
                        lastName.value = user.lastName
                        email.value = user.email
                        companyRole.value = user.companyRole
                        userName.value = "${user.firstName} ${user.lastName}"
                    }
                } else {
                    // Handle error
                    // You might want to set an error state or show a message
                }
            } catch (e: Exception) {
                // Handle network or other errors
                e.printStackTrace()
            }
        }
    }
}