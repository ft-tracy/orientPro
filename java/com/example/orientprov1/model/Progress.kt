package com.example.orientprov1.model

data class Progress(
    val UserId: String,
    val CourseId: String,
    val ModuleId: String,
    val ContentId: String,
    val ContentType: String,
    val progress: Int,
    val LastQuestionIndex: Int?,
    val QuizAnswers: Map<String, Any>,
    val quizScore: Int?
)