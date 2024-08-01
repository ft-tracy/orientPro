package com.example.orientprov1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.repository.QuizRepository

class QuizViewModelFactory(private val repository: QuizRepository, private val apiService: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(repository, apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}