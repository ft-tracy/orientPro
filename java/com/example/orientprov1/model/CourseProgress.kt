package com.example.orientprov1.model

data class CourseProgress(
    val completedContents: List<String>,
    val hasStarted: Boolean,
    val isCompleted: Boolean,
    val progress: Int
)
