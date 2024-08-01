package com.example.orientprov1.model.repository

import com.example.orientprov1.model.Comment
import com.example.orientprov1.model.ContentDetails
import com.example.orientprov1.model.Course
import com.example.orientprov1.model.LastAccessedContent
import com.example.orientprov1.model.Module
import com.example.orientprov1.model.ModuleDetails
import com.example.orientprov1.model.Progress
import com.example.orientprov1.model.api.ApiService
import retrofit2.Response

class CourseRepository(private val apiService: ApiService) {

    suspend fun getCourseDetails(courseId: String): Course {
        return apiService.getCourseDetails(courseId)
    }
    suspend fun getModules(courseId: String): List<Module> {
        return apiService.getModules(courseId)
    }
    suspend fun getModuleDetails(moduleId: String): ModuleDetails {
        return apiService.getModuleDetails(moduleId)
    }
    suspend fun getContentDetails(moduleId: String): ContentDetails {
        return apiService.getContentDetails(moduleId)
    }
    suspend fun getProgress(userId: String, contentId: String): Progress {
        return apiService.getProgress(userId, contentId)
    }

    suspend fun getRecentCourses(userId: String): Course {
        val responseContent: LastAccessedContent = apiService.getLastAccessedContent(userId)
        val responseCourse: Course = apiService.getCourseDetails(responseContent.courseId)
        return responseCourse
    }

    suspend fun getSimilarCourses(token: String, userId: String): List<Course> {
        // Get the recent course
        val recentCourse = getRecentCourses(userId)

        // Fetch the list of available courses
        val response: Response<List<Course>> = apiService.getAvailableCourses(token)

        // If the response is successful, filter the courses by matching tags
        if (response.isSuccessful) {
            val availableCourses = response.body() ?: emptyList()

            // Compare tags of each available course with the recent course's tags
            val similarCourses = availableCourses.filter { course ->
                course.courseTags.any { it in recentCourse.courseTags }
            }

            return similarCourses
        } else {
            // Handle error case (e.g., return an empty list or throw an exception)
            return emptyList()
        }
    }


    suspend fun getCommentsForVideo(videoId: String): List<Comment> {
        // Replace with actual API call
        return apiService.getComments(videoId)
    }

    suspend fun addComment(userId: String, videoId: String, commentText: String): Result<Unit> {
        return try {
            val response: Response<Unit> = apiService.addCommentToVideo(videoId, userId, commentText)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to add comment"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun likeComment(videoId: String, commentId: String, likes: Int): Result<Unit> {
        return try {
            val response: Response<Unit> = apiService.likeComment(videoId, commentId, likes)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update comment likes"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
