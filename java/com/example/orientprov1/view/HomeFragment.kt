package com.example.orientprov1.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.orientprov1.databinding.FragmentHomeBinding
import com.example.orientprov1.model.Result
import com.example.orientprov1.model.UserResponse
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService
import com.example.orientprov1.model.repository.UserRepository
import com.example.orientprov1.viewmodel.MainViewModel
import com.example.orientprov1.viewmodel.NewUserViewModel
import com.example.orientprov1.viewmodel.NewUserViewModelFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.navigation.fragment.NavHostFragment
import com.example.orientprov1.R

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NewUserViewModel
    private lateinit var mainViewModel: MainViewModel

    private var lastDataFetchResult: Result<Any> = Result.Loading
    private var lastServerResponseCode: Int = 0

    private val apiService: ApiService by lazy {
        ApiClient.createService(ApiService::class.java)
    }

    private val sharedPreferences: SharedPreferences by lazy {
        requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    }

    private val userRepository: UserRepository by lazy {
        UserRepository(apiService, sharedPreferences)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d("HomeFragment", "onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("HomeFragment", "onViewCreated called")

        val factory = NewUserViewModelFactory(apiService)
        viewModel = ViewModelProvider(this, factory).get(NewUserViewModel::class.java)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        mainViewModel.userDetails.observe(viewLifecycleOwner) { user ->
            updateUI(user)
        }

        loadData()
    }

    private fun loadData() {
        // Assuming you have a way to get the user token
        val userToken = mainViewModel.token
        userToken?.let {
            viewModel.loadUserData(it)
        }
    }

    private fun updateUI(user: UserResponse) {
        val errorFragment = hasError()
        Log.d("HomeFragment", "Checking for errors")
        when {
            errorFragment != null -> {
                Log.d("HomeFragment", "Error found: $errorFragment")
                showErrorFragment(errorFragment)
            }
            user.role == "Guest" && user.enrolledCourses.isEmpty() -> {
                Log.d("HomeFragment", "Showing first-time user view for guest with no courses")
                showFirstTimeUserView(user)
            }
            user.role == "Trainee" || (user.role == "Guest" && user.enrolledCourses.isNotEmpty()) -> {
                Log.d("HomeFragment", "Showing returning user view")
                showReturningUserView(user)
            }
            else -> {
                Log.d("HomeFragment", "Defaulting to showing returning user view")
                showReturningUserView(user)
            }
        }
    }

    private fun showErrorFragment(errorFragment: ErrorFragment) {
        binding.errorView.root.visibility = View.VISIBLE
        binding.firstTimeUserView.root.visibility = View.GONE
        binding.returningUserView.root.visibility = View.GONE

        // Update error view content
        binding.errorView.apply {
            // Set error message, icon, etc.
        }
    }

    private fun showFirstTimeUserView(user: UserResponse) {
        binding.errorView.root.visibility = View.GONE
        binding.firstTimeUserView.root.visibility = View.VISIBLE
        binding.returningUserView.root.visibility = View.GONE

        // Navigate to NewUserFragment with Bundle
        val token = mainViewModel.token
        val bundle = Bundle().apply {
            putString("USER_TOKEN", token)
        }

        NavHostFragment.findNavController(this@HomeFragment)
            .navigate(R.id.action_HomeFragment_to_NewUserFragment, bundle)
        // Update first-time user view content
        binding.firstTimeUserView.apply {
            Log.d("HomeFragment", "Setting up first-time user view with user: $user")
            // Set user name, available courses, etc.
            // For example:
        }


    }

    private fun showReturningUserView(userResponse: UserResponse) {
        binding.errorView.root.visibility = View.GONE
        binding.firstTimeUserView.root.visibility = View.GONE
        binding.returningUserView.root.visibility = View.VISIBLE

        // Hide helpful tips if the user is a guest
        if (userResponse.role == "Guest") {
            binding.returningUserView.rvHelpfulTips.visibility = View.GONE
        } else {
            binding.returningUserView.rvHelpfulTips.visibility = View.VISIBLE
        }

        // Navigate to NewUserFragment with Bundle
        val token = mainViewModel.token
        val bundle = Bundle().apply {
            putString("USER_TOKEN", token)
        }

        NavHostFragment.findNavController(this@HomeFragment)
            .navigate(R.id.action_HomeFragment_to_ReturningUserFragment, bundle)
        // Update first-time user view content
        binding.firstTimeUserView.apply {
            Log.d("HomeFragment", "Setting up return user view with user: $userResponse")
            // Set user name, available courses, etc.
            // For example:
        }
    }

    private fun hasError(): ErrorFragment? {
        // Check for different types of errors
        return when {
            isNetworkError() -> ErrorFragment.newInstance(com.example.orientprov1.model.error.ErrorType.NETWORK)
            isServerError() -> ErrorFragment.newInstance(com.example.orientprov1.model.error.ErrorType.SERVER)
            isDataError() -> ErrorFragment.newInstance(com.example.orientprov1.model.error.ErrorType.DATA)
            else -> null
        }
    }

    private fun isNetworkError(): Boolean {
        // Implement logic to check for network errors
        // For example:
        return !isNetworkAvailable()
    }

    private fun isServerError(): Boolean {
        // Implement logic to check for server errors
        // For example:
        return lastServerResponseCode in 500..599
    }

    private fun isDataError(): Boolean {
        // Implement logic to check for data errors
        // For example:
        return lastDataFetchResult is Result.Error
    }

    // Helper method to check network availability
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
