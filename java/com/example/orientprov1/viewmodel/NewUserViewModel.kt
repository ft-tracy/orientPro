package com.example.orientprov1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.Course
import com.example.orientprov1.model.HelpfulTip
import com.example.orientprov1.model.UserResponse
import com.example.orientprov1.model.api.ApiService
import kotlinx.coroutines.launch
import retrofit2.Response

class NewUserViewModel(private val apiService: ApiService) : ViewModel() {
    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> = _user

    private val _courses = MutableLiveData<List<Course>?>()
    val courses: LiveData<List<Course>?> = _courses

    private val _helpfulTips = MutableLiveData<List<HelpfulTip>>()
    val helpfulTips: LiveData<List<HelpfulTip>> = _helpfulTips

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadUserData(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUser("Bearer $token")
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _error.value = "Failed to load user data: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error loading user data: ${e.message}"
            }
        }
    }

    fun loadCourses(token: String) {
        viewModelScope.launch {
            Log.d("NewUserViewModel", "Loading available courses with token: $token")
            try {
                val response = apiService.getCourses()
                Log.d("NewUserViewModel", "Response Code: ${response.code()}")
                Log.d("NewUserViewModel", "Response Message: ${response.message()}")
                Log.d("NewUserViewModel", "Response Body: ${response.body()}")

                if (response.isSuccessful) {
                    val courses = response.body()
                    Log.d("NewUserViewModel", "Courses received: $courses")
                    _courses.value = courses
                } else {
                    Log.e("NewUserViewModel", "Failed to load courses: ${response.message()}")
                    _error.value = "Failed to load courses: ${response.message()}"
                }
            } catch (e: Exception) {
                Log.e("NewUserViewModel", "Error loading courses: ${e.message}")
                _error.value = "Error loading courses: ${e.message}"
            }
        }
    }


    fun loadHelpfulTips() {
        viewModelScope.launch {
            Log.d("NewUserViewModel", "Loading tips")
            try {
                val response = apiService.getTips()
                Log.d("NewUserViewModel", "Tips: $response")
                if (response.isSuccessful) {
                    _helpfulTips.value = response.body()
                    Log.d("NewUserViewModel", "Tips: $response.body")
                } else {
                    _error.value = "Failed to load helpful tips: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error loading helpful tips: ${e.message}"
            }
        }
    }
}