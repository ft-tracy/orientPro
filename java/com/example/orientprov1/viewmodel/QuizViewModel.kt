package com.example.orientprov1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.Progress
import com.example.orientprov1.model.QuizDetails
import com.example.orientprov1.model.QuizWithProgress
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.repository.QuizRepository
import com.example.orientprov1.model.UserResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import retrofit2.Response

class QuizViewModel(private val repository: QuizRepository, private val apiService: ApiService) : ViewModel() {

    private val _user = MutableLiveData<UserResponse?>()
    val user: MutableLiveData<UserResponse?> = _user

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _quizDetailsWithProgress = MutableLiveData<List<QuizWithProgress>>()
    val quizDetailsWithProgress: LiveData<List<QuizWithProgress>> get() = _quizDetailsWithProgress

    fun fetchAllQuizzesWithProgress(token: String) {
        Log.d("QuizViewModel", "fetchAllQuizzesWithProgress called")
        viewModelScope.launch {
            try {
                // Fetch user data
                val userResponse = apiService.getUser("Bearer $token")
                Log.d("QuizViewModel", "API Response: $userResponse")
                if (!userResponse.isSuccessful) {
                    _error.value = "Failed to load user data: ${userResponse.message()}"
                    return@launch
                }

                val userData = userResponse.body()
                if (userData == null) {
                    _error.value = "User data is null"
                    return@launch
                }

                val userId = userData.documentId // Assuming the response has a documentId field
                if (userId == null) {
                    _error.value = "User ID is null"
                    return@launch
                }

                Log.d("QuizViewModel", "UserResponse Data: $user")
                _user.value = userData

                // Fetch enrolled courses
                Log.d("QuizViewModel", "Loading available courses with token: $token")
                val enrolledCoursesResponse = repository.getEnrolledCourses("Bearer $token")
                if (!enrolledCoursesResponse.isSuccessful) {
                    _error.value = "Failed to load enrolled courses: ${enrolledCoursesResponse.message()}"
                    return@launch
                }

                val enrolledCourses = enrolledCoursesResponse.body() ?: emptyList()
                Log.d("QuizViewModel", "Courses received: $enrolledCourses")

                val allQuizzes = mutableListOf<QuizWithProgress>()
                val progressPromises = mutableListOf<Deferred<Progress>>()

                for (course in enrolledCourses) {
                    val modules = repository.getModules(course.id)
                    Log.d("QuizViewModel", "Modules received: $modules")
                    for (module in modules) {
                        val contentDetails = repository.getContentDetails(module.moduleId)
                        Log.d("QuizViewModel", "Content details received: $contentDetails")
                        for (quiz in contentDetails.quizzes!!) {
                            val quizData = repository.getQuizzes(module.moduleId, quiz.quizId)
                            Log.d("QuizViewModel", "Quizzes received: $quizData")
                            progressPromises.add(async { repository.getProgress(userId, quiz.quizId) })
                            allQuizzes.add(
                                QuizWithProgress(
                                    quizDetails = quizData,
                                    moduleName = module.moduleName,
                                    status = "Not Started",  // Default status
                                    quizScore = null  // Default score
                                )
                            )
                        }
                    }
                }

                val progressResults = progressPromises.awaitAll()
                val quizzesWithProgress = allQuizzes.mapIndexed { index, quizWithProgress ->
                    val progress = progressResults[index]
                    quizWithProgress.copy(
                        status = when {
                            progress.progress == 100 -> "Completed"
                            progress.progress > 0 -> "In Progress"
                            else -> "Not Started"
                        }
                    )
                }
                _quizDetailsWithProgress.postValue(quizzesWithProgress)
            } catch (e: Exception) {
                _error.value = "Error loading data: ${e.message}"
            }
        }
    }
}