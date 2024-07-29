package com.example.orientprov1.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orientprov1.R
import com.example.orientprov1.databinding.FragmentCourseVideoBinding
import com.example.orientprov1.viewmodel.CourseVideoViewModel
import com.example.orientprov1.viewmodel.CourseVideoViewModelFactory

class CourseVideoFragment : Fragment() {

    private lateinit var viewModel: CourseVideoViewModel
    private lateinit var binding: FragmentCourseVideoBinding
    private lateinit var userId: String
    private lateinit var videoId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getString("userId") ?: ""
        videoId = arguments?.getString("videoId") ?: ""

        viewModel = ViewModelProvider(this, CourseVideoViewModelFactory())[CourseVideoViewModel::class.java]

        setupRecyclerView()
        setupCommentInput()
        observeViewModel()

        // Fetch initial video details and comments
        viewModel.loadVideoDetails(videoId)
        viewModel.loadComments(videoId)
    }

    private fun setupRecyclerView() {
        binding.rvComments.layoutManager = LinearLayoutManager(context)
        binding.rvComments.adapter = CommentAdapter { commentId, newLikes ->
            viewModel.likeComment(videoId, commentId, newLikes)
        }
    }

    private fun setupCommentInput() {
        binding.etAddComment.setOnFocusChangeListener { _, hasFocus ->
            binding.btnAddComment.visibility = if (hasFocus) View.VISIBLE else View.GONE
        }

        binding.btnAddComment.setOnClickListener {
            val commentText = binding.etAddComment.text.toString()
            if (commentText.isNotBlank()) {
                viewModel.addComment(userId, videoId, commentText)
                binding.etAddComment.text.clear()
                binding.etAddComment.clearFocus()
                binding.btnAddComment.visibility = View.GONE
            }
        }

        // Hide button when clicking outside
        binding.root.setOnClickListener {
            binding.etAddComment.clearFocus()
            binding.btnAddComment.visibility = View.GONE
        }
    }

    private fun observeViewModel() {
        viewModel.comments.observe(viewLifecycleOwner) { comments ->
            (binding.rvComments.adapter as CommentAdapter).submitList(comments)
        }

        viewModel.videoDetails.observe(viewLifecycleOwner) { videoDetails ->
            // Update UI with video details
            binding.videoTitle.text = videoDetails.title
            binding.videoDescription.text = videoDetails.description
            // Load video using video URL
            // Example: loadVideo(videoDetails.videoUrl)
        }
    }
}
