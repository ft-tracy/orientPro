package com.example.orientprov1.model

import java.util.Date

data class Comment(
    val id: String,
    val userId: String,
    val videoId: String,
    val date: Date,
    val text: String,
    var likes: Int = 0,
    var replies: List<Reply> = emptyList()
)
