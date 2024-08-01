package com.example.orientprov1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.repository.CourseRepository

class ReturningUserViewModelFactory(
    private val apiService: ApiService,
    private val courseRepository: CourseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReturningUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReturningUserViewModel(apiService, courseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}