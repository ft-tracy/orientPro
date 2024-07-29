package com.example.orientprov1.model

data class ResetPasswordRequest(
    val email: String,
    val newPassword: String,
    val confirmPassword: String
)
