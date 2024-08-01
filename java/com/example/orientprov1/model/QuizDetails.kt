package com.example.orientprov1.model

import java.util.*

data class QuizDetails(
    val quizId: String = "",
    val title: String,
    val questions: List<Question> = listOf(),
    val createdDate: Date = Date(),
    val createdByUserId: String = "",
    val moduleId: String = "",
    val isCompleted: Boolean = false
) {
    data class Question(
        val questionText: String = "",
        val choices: List<String> = listOf(),
        val correctAnswers: List<String> = listOf()
    )
}
