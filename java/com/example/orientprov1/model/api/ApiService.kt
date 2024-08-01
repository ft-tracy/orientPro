package com.example.orientprov1.model.api

import androidx.lifecycle.LiveData
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
    //Login
    @POST("api/account/Login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    //Signup
    @POST("api/account/SignUp")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    //ResetPassword
    @POST("api/account/ResetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>




    //Get user data
    @GET("api/user/getUser")
    suspend fun getUser(@Header("Authorization") token: String): Response<UserResponse>



    //Courses
    //Get all courses
    @GET("courses/getCourses")
    suspend fun getCourses(): Response<List<Course>>

    //Get enrolled courses
    @GET("courses/getEnrolledCourses")
    suspend fun getEnrolledCourses(@Header("Authorization") token: String): Response<List<Course>>

    //Get available courses
    @GET("courses/getAvailableCourses")
    suspend fun getAvailableCourses(@Header("Authorization") token: String): Response<List<Course>>

    //Get completed courses
    @GET("courses/getCompletedCourses")
    suspend fun getCompletedCourses(@Header("Authorization") token: String): Response<List<Course>>



    //Dashboard
    //Get tips
    @GET("api/account/getTips")
    suspend fun getTips(): Response<List<HelpfulTip>>

    @POST("api/account/addTip/{userId}")
    suspend fun addTip(@Path("userId") userId: String, @Body tipContent: HelpfulTip): Response<HelpfulTip>

    @GET("api/Progress/userAccessData/{userId}")
    suspend fun getWeeklyProgress(@Path("userId") userId: String): Response<UserAccessData>



    //Sidebar menu
    //Get course details
    @GET("courses/{courseId}")
    suspend fun getCourseDetails(@Path("courseId") courseId: String): Course

    //Get modules of a specific course
    @GET("api/modules/{courseId}/modules")
    suspend fun getModules(@Path("courseId") courseId: String): List<Module>

    //Get details of a specific module
    @GET("api/modules/{moduleId}")
    suspend fun getModuleDetails(@Path("moduleId") moduleId: String): ModuleDetails

    //Get contents for a specific module
    @GET("content/getModuleContents/{moduleId}")
    suspend fun getContentDetails(@Path("moduleId") moduleId: String): ContentDetails



    //CoursePage
    //Fetch reviews
    @GET("api/reviews/course/{courseId}")
    suspend fun getReviews(@Path("courseId") courseId: String)

    //Add a review
    @POST("api/reviews/{userId}/review/{courseId}")
    suspend fun addReview(
        @Path("userId") userId: String,
        @Path("courseId") courseId: String,
        @Body review: String
    )

    //Enroll user in course
    @POST("api/user/{userId}/enroll/{courseId}")
    suspend fun enrollCourse(
        @Path("userId") userId: String,
        @Path("courseId") courseId: String
    )

    //Change hasStarted flag to true
    @POST("api/user/{userId}/start/{courseId}")
    suspend fun startCourse(
        @Path("userId") userId: String,
        @Path("courseId") courseId: String
    )



    //Content pages
    //Get progress
    @GET("api/progress/getProgress/{userId}/{contentId}")
    suspend fun getProgress(
        @Path("userId") userId: String,
        @Path("contentId") contentId: String
    ): Progress

    //Send progress updates
    @POST("api/progress/updateProgress")
    suspend fun updateProgress(@Body progressData: Progress): Response<Any>

    //Get last accessed content
    @GET("api/progress/lastAccessedContent/{userId}")
    suspend fun getLastAccessedContent(@Path("userId") userId: String): LastAccessedContent


    //Video Content Page
    @GET("content/GetCommentsAndRepliesForVideo/{videoId}")
    suspend fun getComments(@Path("videoId") videoId: String): List<Comment>

    @POST("Content/AddCommentToVideo/{videoId}/{userId}")
    suspend fun addCommentToVideo(
        @Path("videoId") videoId: String,
        @Path("userId") userId: String,
        @Body commentText: String
    ): Response<Unit>

    @PUT("Content/likeComment/{videoId}/{commentId}")
    suspend fun likeComment(
        @Path("videoId") videoId: String,
        @Path("commentId") commentId: String,
        @Body likes: Int
    ): Response<Unit>

    //Get quizzes
    @GET("content/getQuiz/{moduleId}/{quizId}")
    suspend fun getQuizzes(
        @Path("moduleId") moduleId: String,
        @Path("quizId") quizId: String
    ): Response<List<QuizDetails>>
}
