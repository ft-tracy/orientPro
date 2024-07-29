package com.example.orientprov1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.repository.UserRepository
import kotlinx.coroutines.launch
import com.example.orientprov1.model.Result

class UpdateUserInfoViewModel(private val repository: UserRepository) : ViewModel() {
    private val _updateResult = MutableLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> = _updateResult

    fun updateUserInfo(email: String, newPassword: String, confirmPassword: String) {
        viewModelScope.launch {
            _updateResult.value = Result.Loading
            val result = repository.resetPassword(email, newPassword, confirmPassword)
            _updateResult.value = when {
                result.isSuccess -> Result.Success(result.getOrNull() ?: "Password reset successful")
                result.isFailure -> Result.Error(result.exceptionOrNull()?.message ?: "An error occurred")
                else -> Result.Error("Unknown error")
            }
        }
    }
}