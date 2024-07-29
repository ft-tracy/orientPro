package com.example.orientprov1.model

data class CourseSection(
val title: String,
val courses: List<Course>,
val isAvailable: Boolean = false
)
