package com.example.orientprov1.model.api

import com.example.orientprov1.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("api/account/Login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/account/SignUp")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("api/account/ResetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @GET("api/user/getuser")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserResponse>

    @GET("content/GetCommentsAndRepliesForVideo/{videoId}")
    suspend fun getComments(@Path("videoId") videoId: String): List<Comment>

    @POST("/Content/AddCommentToVideo/{videoId}/{userId}")
    suspend fun addCommentToVideo(
        @Path("videoId") videoId: String,
        @Path("userId") userId: String,
        @Body commentText: String
    ): Response<Unit>

    @PUT("/Content/likeComment/{videoId}/{commentId}")
    suspend fun likeComment(
        @Path("videoId") videoId: String,
        @Path("commentId") commentId: String,
        @Body likes: Int
    ): Response<Unit>

    /*@GET("recent-courses")
    fun getRecentCourses(): Call<List<Course>>

    @GET("similar-courses")
    fun getSimilarCourses(): Call<List<Course>>

    @GET("weekly-progress")
    fun getWeeklyProgress(): Call<List<DayProgress>>

    @GET("helpful-tips")
    fun getHelpfulTips(): Call<List<Tip>>*/
}
