package com.example.orientprov1.model

data class LastAccessedContent(
    val contentId: String,
    val contentType: String,
    val courseId: String,
    val lastQuestionIndex: Int,
    val moduleId: String,
    val progress: Int,
    val quizAnswers: Map<String, Any>,
    val quizScore: Int?,
    val userId: String
)
