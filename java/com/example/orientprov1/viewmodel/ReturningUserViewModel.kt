package com.example.orientprov1.viewmodel

import android.media.session.MediaSession.Token
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.Course
import com.example.orientprov1.model.HelpfulTip
import com.example.orientprov1.model.UserAccessData
import com.example.orientprov1.model.UserResponse
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.repository.CourseRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ReturningUserViewModel(
    private val apiService: ApiService,
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _userDetails = MutableLiveData<UserResponse>()
    val userDetails: LiveData<UserResponse> = _userDetails

    private val _recentCourses = MutableLiveData<List<Course>>()
    val recentCourses: LiveData<List<Course>> = _recentCourses

    private val _similarCourses = MutableLiveData<List<Course>>()
    val similarCourses: LiveData<List<Course>> = _similarCourses

    private val _weeklyProgress = MutableLiveData<Response<UserAccessData>>()
    val weeklyProgress: LiveData<Response<UserAccessData>> = _weeklyProgress

    private val _helpfulTips = MutableLiveData<List<HelpfulTip>>()
    val helpfulTips: LiveData<List<HelpfulTip>> = _helpfulTips

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

//    init {
//        fetchUserDetails()
//        fetchRecentCourses()
//        fetchSimilarCourses()
//        fetchWeeklyProgress()
//        fetchHelpfulTips()
//    }

    fun fetchUserDetails(token: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getUser("Bearer $token")
                if (response.isSuccessful) {
                    _userDetails.value = response.body()
                } else {
                    _error.value = "Failed to load user data: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error loading user data: ${e.message}"
            }
        }
    }

    private fun fetchRecentCourses(userId: String) {
        viewModelScope.launch {
            try {
                val courses = courseRepository.getRecentCourses(userId)
                _recentCourses.value = listOf(courses)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun fetchSimilarCourses(token: String, userId: String) {
        viewModelScope.launch {
            try {
                val courses = courseRepository.getSimilarCourses(token, userId)
                _similarCourses.value = courses
            } catch (e: Exception) {
                // Handle error, e.g., log the error or update UI with error message
                Log.e("CourseViewModel", "Error fetching similar courses", e)
            }
        }
    }


    fun fetchWeeklyProgress(userId: String) {
        viewModelScope.launch {
            try {
                val progress = apiService.getWeeklyProgress(userId)
                _weeklyProgress.value = progress
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun fetchHelpfulTips() {
        viewModelScope.launch {
            Log.d("ReturningUserViewModel", "Loading tips")
            try {
                val response = apiService.getTips()
                Log.d("ReturningUserViewModel", "Courses: $response")
                if (response.isSuccessful) {
                    _helpfulTips.value = response.body()
                    Log.d("ReturningUserViewModel", "Courses: $response.body")
                } else {
                    _error.value = "Failed to load helpful tips: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Error loading helpful tips: ${e.message}"
            }
        }
    }
}