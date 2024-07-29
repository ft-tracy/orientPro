package com.example.orientprov1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.Comment
import com.example.orientprov1.model.VideoDetails
import com.example.orientprov1.model.repository.CourseRepository
import kotlinx.coroutines.launch

class CourseVideoViewModel(private val repository: CourseRepository) : ViewModel() {

    private val _videoDetails = MutableLiveData<VideoDetails>()
    val videoDetails: LiveData<VideoDetails> get() = _videoDetails

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> get() = _comments

    private val _newCommentText = MutableLiveData<String>()
    val newCommentText: LiveData<String> get() = _newCommentText

    fun loadVideoDetails(videoId: String) {
        // Load video details from backend
        viewModelScope.launch {
            _videoDetails.value = repository.loadVideoDetails()
        }
    }

    fun loadComments(videoId: String) {
        viewModelScope.launch {
            _comments.value = repository.getCommentsForVideo(videoId)
        }
    }

    fun addComment(userId: String, videoId: String, commentText: String) {
        viewModelScope.launch {
            val result = repository.addComment(userId, videoId, commentText)
            result.onSuccess {
                // Handle success (e.g., update UI, fetch new comments, etc.)
            }.onFailure {
                // Handle failure (e.g., show error message)
            }
        }
    }

    fun likeComment(videoId: String, commentId: String, likes: Int) {
        viewModelScope.launch {
            val result = repository.likeComment(videoId, commentId, likes)
            result.onSuccess {
                // Handle success (e.g., update UI)
            }.onFailure {
                // Handle failure (e.g., show error message)
            }
        }
    }

    fun onNewCommentTextChanged(text: CharSequence) {
        _newCommentText.value = text.toString()
    }
}
