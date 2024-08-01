package com.example.orientprov1.model

data class UserResponse(
    val documentId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val hasAccess: Boolean,
    val isFirstLogin: Boolean,
    val otp: String?,
    val otpExpiration: String?, // Assuming it's a String; change if needed
    val pwd: String,
    val role: String,
    val enrolledCourses: List<String>,
    val courseProgress: Map<String, CourseProgress>
)
