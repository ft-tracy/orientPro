package com.example.orientprov1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.Comment
import com.example.orientprov1.model.Content
import com.example.orientprov1.model.ContentType
import com.example.orientprov1.model.VideoDetails
import com.example.orientprov1.model.repository.CourseRepository
import kotlinx.coroutines.launch

class CourseVideoViewModel(private val repository: CourseRepository) : BaseCourseContentViewModel(repository) {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _newCommentText = MutableLiveData<String>()
    val newCommentText: LiveData<String> get() = _newCommentText

    fun loadContentSpecificData(content: Content?, userId: String?) {
        if (content?.type == ContentType.VIDEO) {
            loadComments(content.id)
        }
    }

    private fun loadComments(videoId: String) {
        viewModelScope.launch {
            val comments = repository.getCommentsForVideo(videoId)
            _comments.value = comments
        }
    }

    fun addComment(userId: String, videoId: String, commentText: String) {
        viewModelScope.launch {
            repository.addComment(userId, videoId, commentText)
            loadComments(videoId)
        }
    }

    fun likeComment(videoId: String, commentId: String, newLikes: Int) {
        viewModelScope.launch {
            repository.likeComment(videoId, commentId, newLikes)
            loadComments(videoId)
        }
    }

    fun onNewCommentTextChanged(text: CharSequence) {
        _newCommentText.value = text.toString()
    }
}
