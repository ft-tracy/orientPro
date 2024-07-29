package com.example.orientprov1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.SignUpRequest
import com.example.orientprov1.model.SignUpResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    private val _signUpResult = MutableLiveData<Result<SignUpResponse>>()
    val signUpResult: LiveData<Result<SignUpResponse>> get() = _signUpResult

    fun signUp(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
        val signUpRequest = SignUpRequest(firstName, lastName, email, password, confirmPassword)
        val apiService = ApiClient.createService(ApiService::class.java)

        viewModelScope.launch {
            try {
                val response: Response<SignUpResponse> = apiService.signUp(signUpRequest)
                if (response.isSuccessful) {
                    _signUpResult.value = Result.success(response.body()!!)
                } else {
                    _signUpResult.value = Result.failure(Exception(response.message()))
                }
            } catch (e: HttpException) {
                _signUpResult.value = Result.failure(Exception(e.message()))
            } catch (e: Exception) {
                _signUpResult.value = Result.failure(e)
            }
        }
    }
}
