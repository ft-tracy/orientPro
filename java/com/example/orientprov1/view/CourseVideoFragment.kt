//package com.example.orientprov1.view
//
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.orientprov1.R
//import com.example.orientprov1.databinding.FragmentCourseVideoBinding
//import com.example.orientprov1.model.CourseState
//import com.example.orientprov1.model.VideoDetails
//import com.example.orientprov1.model.api.ApiClient
//import com.example.orientprov1.model.api.ApiService
//import com.example.orientprov1.model.repository.CourseRepository
//import com.example.orientprov1.model.repository.UserRepository
//import com.example.orientprov1.viewmodel.CourseVideoViewModel
//import com.example.orientprov1.viewmodel.CourseVideoViewModelFactory
//import kotlinx.coroutines.launch
//
//class CourseVideoFragment : BaseContentFragment() {
//
//    private lateinit var binding: FragmentCourseVideoBinding
//    private lateinit var viewModel: CourseVideoViewModel
//    private lateinit var userId: String
//    private lateinit var videoId: String
//    private lateinit var courseId: String
//    private lateinit var moduleId: String
//
//    val apiService = ApiClient.createService(ApiService::class.java)
//    val courseRepository = CourseRepository(apiService)
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        binding = FragmentCourseVideoBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        userId = arguments?.getString("userId") ?: ""
//        videoId = arguments?.getString("videoId") ?: ""
//        courseId = arguments?.getString("courseId") ?: ""
//        moduleId = arguments?.getString("moduleId") ?: ""
//
//        viewModel = ViewModelProvider(this, CourseVideoViewModelFactory(courseRepository))[CourseVideoViewModel::class.java]
//
//        setupRecyclerView()
//        setupCommentInput()
//        observeViewModel()
//
//        viewModel.loadCourseData(courseId, videoId, moduleId, userId)
//    }
//
//    private fun setupRecyclerView() {
//        binding.rvComments.layoutManager = LinearLayoutManager(context)
//        binding.rvComments.adapter = CommentAdapter { commentId, newLikes ->
//            viewModel.likeComment(videoId, commentId, newLikes)
//        }
//    }
//
//    private fun setupCommentInput() {
//        binding.etAddComment.setOnFocusChangeListener { _, hasFocus ->
//            binding.btnAddComment.visibility = if (hasFocus) View.VISIBLE else View.GONE
//        }
//
//        binding.btnAddComment.setOnClickListener {
//            val commentText = binding.etAddComment.text.toString()
//            if (commentText.isNotBlank()) {
//                viewModel.addComment(userId, videoId, commentText)
//                binding.etAddComment.text.clear()
//                binding.etAddComment.clearFocus()
//                binding.btnAddComment.visibility = View.GONE
//            }
//        }
//
//        binding.root.setOnClickListener {
//            binding.etAddComment.clearFocus()
//            binding.btnAddComment.visibility = View.GONE
//        }
//    }
//
//    private fun observeViewModel() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.state.collect { state ->
//                    updateUIWithCommonState(state)
//                }
//            }
//        }
//
//        viewModel.comments.observe(viewLifecycleOwner) { comments ->
//            (binding.rvComments.adapter as CommentAdapter).submitList(comments)
//        }
//    }
//
//    private fun updateUIWithCommonState(state: CourseState) {
//        // Safely cast currentContent?.data to VideoDetails
//        val videoDetails = state.currentContent?.data as? VideoDetails
//
//        if (videoDetails != null) {
//            binding.tvVideoTitle.text = videoDetails.title
//            binding.videoDescription.text = videoDetails.description
//        } else {
//            // Handle the case where currentContent?.data is not VideoDetails
//            binding.tvVideoTitle.text = "Title not available"
//            binding.videoDescription.text = "Description not available"
//        }
//    }
//
//
//}
