package com.example.orientpro.utils

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val success: Boolean, val message: String, val token: String?, val isFirstLogin: Boolean)
data class OTPRequest(val email: String, val otp: String)
data class OTPResponse(val success: Boolean, val message: String)
data class UpdateUserInfoRequest(val email: String, val password: String, val confirmPassword: String)
data class UpdateUserInfoResponse(val success: Boolean, val message: String)
data class SignUpRequest(val firstName: String,val lastName: String,val email: String,val password: String)
data class SignUpResponse(val success: Boolean, val message: String)

interface APIService {

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("validateOtp")
    fun validateOtp(@Body request: OTPRequest): Call<OTPResponse>

    @POST("updateUserInfo")
    fun updateUserInfo(@Body request: UpdateUserInfoRequest): Call<UpdateUserInfoResponse>

    @POST("signup")
    fun signUp(@Body request: SignUpRequest): Call<SignUpResponse>
}
