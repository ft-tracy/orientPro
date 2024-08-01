package com.example.orientprov1.model

data class Content(
    val id: String,
    val type: ContentType,
    val timestamp: String,
    val data: Any,
    val title: String
)
