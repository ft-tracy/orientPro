package com.example.orientprov1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.CourseState
import com.example.orientprov1.model.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoursePageViewModel(private val courseRepository: CourseRepository) : ViewModel() {
    private val _state = MutableStateFlow(CourseState())
    val state: StateFlow<CourseState> get() = _state

    fun loadCourseData(courseId: String) {
        viewModelScope.launch {
            try {
                val course = courseRepository.getCourseDetails(courseId)
                _state.value = _state.value.copy(
                    courseDetails = course,
                    modules = course.modules,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e,
                    isLoading = false
                )
            }
        }
    }

    fun selectModule(moduleId: Int) {
        // Implement module selection logic
    }

    fun navigateToContent(contentId: String) {
        // Implement navigation logic
    }
}
