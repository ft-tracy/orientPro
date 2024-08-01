package com.example.orientprov1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.*
import com.example.orientprov1.model.repository.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BaseCourseContentViewModel(private val repository: CourseRepository) : ViewModel() {

    private val _state = MutableStateFlow(CourseState())
    val state: StateFlow<CourseState> = _state

    fun loadCourseData(courseId: String, contentId: String, moduleId: String, userId: String?) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)

                val courseDetails = repository.getCourseDetails(courseId)
                val modules = repository.getModules(courseId)

                val detailedModules = modules.map { module ->
                    val moduleDetails = listOf(repository.getModuleDetails(module.moduleId))
                    val contentDetails = repository.getContentDetails(module.moduleId)
                    val allContents = assignContentTypes(contentDetails)

                    ModuleWithContents(moduleDetails, allContents)
                }

                val allContents = detailedModules.flatMap { it.contents }
                val currentContent = allContents.find { it.id == contentId }

                val progress = if (currentContent?.type == ContentType.READING) {
                    repository.getProgress(userId ?: "", currentContent.id).progress
                } else 0

                _state.value = _state.value.copy(
                    courseDetails = courseDetails,
//                    modules = detailedModules,
                    allContent = allContents,
                    currentContent = currentContent,
                    contentType = currentContent?.type,
                    currentProgress = progress,
                    isFirstContentOfCourse = detailedModules.firstOrNull()?.contents?.firstOrNull()?.id == contentId,
                    isLastPage = detailedModules.find { it.moduleDetails.any { md -> md.moduleId == moduleId } }?.contents?.lastOrNull()?.id == contentId,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e, isLoading = false)
            }
        }
    }


    private fun assignContentTypes(contentDetails: ContentDetails): List<Content> {
        val allContents = mutableListOf<Content>()

        contentDetails.videos?.forEach { video ->
            allContents.add(Content(id = video.id, type = ContentType.VIDEO, timestamp = video.uploadedDate, data = video, title = video.title))
        }
        contentDetails.readingMaterials?.forEach { reading ->
            allContents.add(Content(id = reading.id, type = ContentType.READING, timestamp = reading.uploadedDate, data = reading, title = reading.title))
        }
        contentDetails.quizzes?.forEach { quiz ->
            allContents.add(Content(id = quiz.quizId, type = ContentType.QUIZ, timestamp = quiz.createdDate.toString(), data = quiz, title = quiz.title))
        }

        return allContents.sortedBy { it.timestamp }
    }
}
