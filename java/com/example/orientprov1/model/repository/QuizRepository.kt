package com.example.orientprov1.repository

import com.example.orientprov1.model.ContentDetails
import com.example.orientprov1.model.Course
import com.example.orientprov1.model.Module
import com.example.orientprov1.model.Progress
import com.example.orientprov1.model.QuizDetails
import com.example.orientprov1.model.api.ApiService
import retrofit2.Response

class QuizRepository(private val apiService: ApiService) {

    suspend fun getEnrolledCourses(token: String): Response<List<Course>> {
        return apiService.getEnrolledCourses(token)
    }

    suspend fun getModules(courseId: String): List<Module> {
        return apiService.getModules(courseId)
    }

    suspend fun getContentDetails(moduleId: String): ContentDetails {
        return apiService.getContentDetails(moduleId)
    }

    suspend fun getQuizzes(moduleId: String, quizId: String): Response<List<QuizDetails>> {
        return apiService.getQuizzes(moduleId, quizId)
    }

    suspend fun getProgress(userId: String, quizId: String): Progress {
        return apiService.getProgress(userId, quizId)
    }
}
