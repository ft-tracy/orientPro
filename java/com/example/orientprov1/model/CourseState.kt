package com.example.orientprov1.model

data class CourseState(
    val showSidebar: Boolean = false,
    val contentType: ContentType? = null,
    val isFirstContentOfCourse: Boolean = false,
    val isLastPage: Boolean = false,
    val courseDetails: Course? = null,
    val modules: List<Module> = emptyList(),
    val currentContent: Content? = null,
    val allContent: List<Content> = emptyList(),
    val isLoading: Boolean = true,
    val currentProgress: Int = 0,
    val error: Throwable? = null
)
