package com.example.orientprov1.model

import retrofit2.Response

data class QuizWithProgress(
    val quizDetails: Response<List<QuizDetails>>,
    val moduleName: String,
    val status: String,
    val quizScore: Int?
)
