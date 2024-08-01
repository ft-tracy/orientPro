//package com.example.orientprov1.view
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Observer
//import com.example.orientprov1.databinding.FragmentBaseContentBinding
//import com.example.orientprov1.viewmodel.BaseCourseContentViewModel
//import com.example.orientprov1.viewmodel.CourseVideoViewModel
//import com.example.orientprov1.model.ContentType
//
//open class BaseContentFragment : Fragment() {
//
//    private var _binding: FragmentBaseContentBinding? = null
//    private val binding get() = _binding!!
//
//    private val baseViewModel: BaseCourseContentViewModel by viewModels()
//    private val videoViewModel: CourseVideoViewModel by viewModels()
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentBaseContentBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Observe state from BaseCourseContentViewModel
//        baseViewModel.state.observe(viewLifecycleOwner, Observer { state ->
//            if (state.contentType == ContentType.VIDEO) {
//                val content = state.currentContent
//                if (content != null) {
//                    videoViewModel.loadContentSpecificData(content, userId = "user_id_here")
//
//                    videoViewModel.videoDetails.observe(viewLifecycleOwner, Observer { videoDetails ->
//                        // Bind video details to your view
//                        binding.videoTitle.text = videoDetails.title
//                        binding.videoDescription.text = videoDetails.description
//                    })
//
//                    videoViewModel.comments.observe(viewLifecycleOwner, Observer { comments ->
//                        // Bind comments to your view
//                    })
//                }
//            }
//        })
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
