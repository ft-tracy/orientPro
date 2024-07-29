package com.example.orientprov1.model.repository

import com.example.orientprov1.model.Comment
import com.example.orientprov1.model.api.ApiService
import retrofit2.Response

class CourseRepository(private val apiService: ApiService) {

    suspend fun loadVideoDetails() {
        return apiService.get
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
