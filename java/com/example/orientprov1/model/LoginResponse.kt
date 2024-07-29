package com.example.orientprov1.model

data class LoginResponse(
    val message: String,
    val token: String? = null,
    val isFirstLogin: Boolean? = null
)