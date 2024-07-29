package com.example.orientprov1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.Course
import com.example.orientprov1.model.HelpfulTip
import kotlinx.coroutines.launch

class NewUserViewModel : ViewModel() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _courses = MutableLiveData<List<Course>>()
    val courses: LiveData<List<Course>> = _courses

    private val _helpfulTips = MutableLiveData<List<HelpfulTip>>()
    val helpfulTips: LiveData<List<HelpfulTip>> = _helpfulTips

    fun loadUserData() {
        viewModelScope.launch {
            _userName.value = getUserName()
        }
    }

    fun loadCourses() {
        viewModelScope.launch {
            _courses.value = getCourses()
        }
    }

    fun loadHelpfulTips() {
        viewModelScope.launch {
            _helpfulTips.value = getHelpfulTips()
        }
    }

    // These functions should be replaced with actual API calls
    private suspend fun getUserName(): String = "New User"
    private suspend fun getCourses(): List<Course> = emptyList()
    private suspend fun getHelpfulTips(): List<HelpfulTip> = emptyList()
}