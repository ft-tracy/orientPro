package com.example.orientprov1.model

import java.util.Date

data class Reply(
    val id: String,
    val userId: String,
    val text: String,
    val date: Date,
    val likes: Int,
    val commentId: String
)
