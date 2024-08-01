//package com.example.orientprov1.view
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.orientprov1.databinding.FragmentCoursePageBinding
//import com.example.orientprov1.model.Course
//import com.example.orientprov1.model.CourseSection
//
//class CoursePageFragment : Fragment() {
//
//    private var _binding: FragmentCoursePageBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var coursesAdapter: CoursesAdapter
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentCoursePageBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupRecyclerView()
//        loadCourses()
//    }
//
//    private fun setupRecyclerView() {
//        coursesAdapter = CoursesAdapter { course ->
//            navigateToCourseIntro(course)
//        }
//        binding.recyclerView.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = coursesAdapter
//        }
//    }
//
//    private fun loadCourses() {
//        // This would typically be done asynchronously
//        val token = getUserToken() // Implement this method to get the user's token
//        val courseSections = listOf(
//            CourseSection("Enrolled Courses", getEnrolledCourses(token)),
//            CourseSection("Completed Courses", getCompletedCourses(token)),
//            CourseSection("Available Courses", getAvailableCourses(), true)
//        ).filter { it.courses.isNotEmpty() }
//        coursesAdapter.submitList(courseSections)
//    }
//
//    private fun getEnrolledCourses(token: String): List<Course> {
//        // Implement API call to get enrolled courses
//        return listOf(
//            Course("Web app Development", "web_app_url"),
//            Course("Mobile Computing", "mobile_computing_url"),
//            Course("CyberSecurity Ops Associate", "cyber_security_url")
//        )
//    }
//
//    private fun getCompletedCourses(token: String): List<Course> {
//        // Implement API call to get completed courses
//        return listOf(
//            Course("Introduction to the computing", "intro_computing_url"),
//            Course("Communication skills", "communication_skills_url"),
//            Course("Public Speaking", "public_speaking_url")
//        )
//    }
//
//    private fun getAvailableCourses(): List<Course> {
//        // Implement API call to get available courses
//        return listOf(
//            Course("Health and Safety", "health_safety_url"),
//            Course("Internet Security", "internet_security_url")
//        )
//    }
//
//    private fun navigateToCourseIntro(course: Course) {
//        // Implement navigation to CourseIntroFragment
//        // For example:
//        // findNavController().navigate(CoursesFragmentDirections.actionCoursesFragmentToCourseIntroFragment(course))
//    }
//
//    private fun getUserToken(): String {
//        // Implement logic to get user token
//        return "user_token"
//    }
//}
