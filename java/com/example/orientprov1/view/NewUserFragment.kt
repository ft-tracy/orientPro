package com.example.orientprov1.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.orientprov1.R
import com.example.orientprov1.databinding.NewuserViewBinding
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.viewmodel.NewUserViewModel
import com.example.orientprov1.viewmodel.NewUserViewModelFactory

class NewUserFragment : Fragment() {

    private var _binding: NewuserViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NewUserViewModel
    private lateinit var courseAdapter: CoursesAdapter
    private lateinit var helpfulTipAdapter: HelpfulTipsAdapter
    private var userToken: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("NewUserFragment", "onCreateView called")
        _binding = NewuserViewBinding.inflate(inflater, container, false)

        // Retrieve the token passed from HomeFragment
        arguments?.let { bundle ->
            userToken = bundle.getString("USER_TOKEN")
        }

        Log.d("NewUserFragment", "Token passed from HomeFragment")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("NewUserFragment", "onViewCreate called")

        // Initialize ApiService
        val apiService = ApiClient.createService(ApiService::class.java)

        // Initialize ViewModel with ViewModelProvider
        val factory = NewUserViewModelFactory(apiService)
        viewModel = ViewModelProvider(this, factory).get(NewUserViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerViews()
        setupObservers()
        loadData()
    }

    private fun setupRecyclerViews() {
        Log.d("NewUserFragment", "setupRecyclerViews called")
        courseAdapter = CoursesAdapter { course ->
            // Navigate to CourseIntroPageFragment on course click
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            Log.d("NewUserFragment", "navHostFragment set up")
            val navController = navHostFragment.navController

            val bundle = Bundle().apply {
                putSerializable("course", course)
            }

            navController.navigate(R.id.action_NewUserFragment_toCourseIntroPageFragment, bundle)
        }
        binding.rvCourses.adapter = courseAdapter
        binding.rvCourses.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        helpfulTipAdapter = HelpfulTipsAdapter()
        binding.rvHelpfulTips.adapter = helpfulTipAdapter
        binding.rvHelpfulTips.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun setupObservers() {
        Log.d("NewUserFragment", "setupObservers called")
        viewModel.user.observe(viewLifecycleOwner) { user ->
            binding.tvGreeting.text = "Hello ${user.firstName}"
        }

        viewModel.courses.observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses)
        }

        viewModel.helpfulTips.observe(viewLifecycleOwner) { tips ->
            helpfulTipAdapter.submitList(tips)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            // Handle error message display
        }
    }

    private fun loadData() {
        Log.d("NewUserFragment", "loadData called")
        userToken?.let { token ->
            viewModel.loadUserData(token)
            viewModel.loadCourses(token)
            viewModel.loadHelpfulTips()
        }
    }

    override fun onDestroyView() {
        Log.d("NewUserFragment", "onDestroyView called")
        super.onDestroyView()
        _binding = null
    }
}

