package com.example.orientpro

data class SignUpRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val confirmPassword: String
)
