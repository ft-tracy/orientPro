package com.example.orientprov1.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orientprov1.databinding.FragmentReturningUserBinding
import com.example.orientprov1.model.UserResponse
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.repository.CourseRepository
import com.example.orientprov1.viewmodel.ReturningUserViewModel
import com.example.orientprov1.viewmodel.ReturningUserViewModelFactory

class ReturningUserFragment : Fragment() {

    private lateinit var binding: FragmentReturningUserBinding
    private lateinit var viewModel: ReturningUserViewModel
    private lateinit var helpfulTipAdapter: HelpfulTipsAdapter
    private var userToken: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("ReturningUserFragment", "onCreateView called")
        binding = FragmentReturningUserBinding.inflate(inflater, container, false)

        // Retrieve the token passed from HomeFragment
        arguments?.let { bundle ->
            userToken = bundle.getString("USER_TOKEN")
        }

        Log.d("ReturningUserFragment", "Token passed from HomeFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerViews()
        observeViewModel()
        fetchData()
    }

    private fun setupViewModel() {
        val apiService = ApiClient.createService(ApiService::class.java)

        val courseRepository = CourseRepository(apiService)
        val factory = ReturningUserViewModelFactory(apiService, courseRepository)
        viewModel = ViewModelProvider(this, factory)[ReturningUserViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupRecyclerViews() {
        binding.recyclerRecentCourses.layoutManager = LinearLayoutManager(context)
        binding.rvCourses.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerWeeklyProgress.layoutManager = LinearLayoutManager(context)

        // TODO: Set adapters for each RecyclerView
        // binding.recyclerRecentCourses.adapter = RecentCoursesAdapter()
        // binding.rvCourses.adapter = SimilarCoursesAdapter()
        // binding.recyclerWeeklyProgress.adapter = WeeklyProgressAdapter()
        helpfulTipAdapter = HelpfulTipsAdapter()
        binding.rvHelpfulTips.adapter = helpfulTipAdapter
        binding.rvHelpfulTips.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.userDetails.observe(viewLifecycleOwner) { userResponse ->
            updateUI(userResponse)
        }

        viewModel.recentCourses.observe(viewLifecycleOwner) { courses ->
            // TODO: Update recent courses RecyclerView
        }

        viewModel.similarCourses.observe(viewLifecycleOwner) { courses ->
            // TODO: Update similar courses RecyclerView
        }

        viewModel.weeklyProgress.observe(viewLifecycleOwner) { progress ->
            // TODO: Update weekly progress RecyclerView
        }

        viewModel.helpfulTips.observe(viewLifecycleOwner) { tips ->
            helpfulTipAdapter.submitList(tips)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // TODO: Handle error, e.g., show a toast or snackbar
        }
    }

    private fun updateUI(userResponse: UserResponse) {
        if (userResponse.role == "Guest") {
            binding.rvHelpfulTips.visibility = View.GONE
        } else {
            binding.rvHelpfulTips.visibility = View.VISIBLE
        }

        // TODO: Update other UI elements based on userResponse
    }

     private fun fetchData() {
        Log.d("ReturningUserFragment", "fetchData called")
        userToken?.let { token ->
            viewModel.fetchUserDetails(token)
//            viewModel.fetchWeeklyProgress(userId)
            viewModel.fetchHelpfulTips()
        }
     }
}