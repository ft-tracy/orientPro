package com.example.orientpro

data class LoginResponse(
    val message: String,
    val token: String? = null,
    val isFirstLogin: Boolean? = null
)
