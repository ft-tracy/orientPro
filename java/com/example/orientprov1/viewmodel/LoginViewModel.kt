package com.example.orientprov1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.LoginResponse
import com.example.orientprov1.model.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.login(email, password)
            _loginResult.postValue(result)
        }
    }

    fun isLoggedIn(): Boolean {
        return userRepository.isLoggedIn()
    }

    fun isFirstLogin(): Boolean {
        return userRepository.isFirstLogin()
    }
}
