package com.example.orientprov1.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.orientprov1.R
import com.example.orientprov1.viewmodel.SettingsViewModel
import com.example.orientprov1.viewmodel.SettingsViewModelFactory
import com.example.orientprov1.databinding.NavSettingsBinding
import com.example.orientprov1.model.repository.DataStoreRepository
import com.example.orientprov1.model.repository.UserRepository
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService

class SettingsView : Fragment() {
    init {
        Log.d("SettingsView", "SettingsView class initialized")
    }
    private lateinit var viewModel: SettingsViewModel
    private var _binding: NavSettingsBinding? = null
    private val binding get() = _binding!!

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SettingsView", "onCreateView called")
        _binding = NavSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SettingsView", "onViewCreated called")
        Log.d("SettingsView", "Profile button visibility: ${binding.btnProfileView.visibility == View.VISIBLE}")

        val apiService = ApiClient.createService(ApiService::class.java)
        val sharedPreferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val userRepository = UserRepository(apiService, sharedPreferences)
        val dataStoreRepository = DataStoreRepository(requireContext().dataStore)
        val viewModelFactory = SettingsViewModelFactory(userRepository, dataStoreRepository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        setupUI()
        Log.d("SettingsView", "UI setup")
        observeViewModel()
        Log.d("SettingsView", "Observing ViewModel")
    }

    private fun setupUI() {
        binding.btnProfileView.setOnClickListener {
            Log.d("SettingsView", "Profile button clicked in setupUI")
            viewModel.onProfileViewClicked()
        }

        binding.btnSignout.setOnClickListener{
            Log.d("SettingsView", "Signout button clicked in setupUI")
            viewModel.onSignedOut()
        }
    }

    private fun observeViewModel() {
        Log.d("SettingsView", "observeViewModel called")
        viewModel.navigateToProfile.observe(viewLifecycleOwner) { shouldNavigate ->
            Log.d("SettingsView", "navigateToProfile observed: $shouldNavigate")
            if (shouldNavigate) {
                val token = (activity as? MainActivity)?.viewModel?.getToken()
                if (token != null) {
                    Log.d("SettingsView", "Token retrieved: $token")
                    try {
                        val bundle = Bundle().apply {
                            putString("token", token)
                        }
                        Log.d("SettingsView", "Bundle created: $bundle")
                        val navController = NavHostFragment.findNavController(this)
                        navController.navigate(R.id.action_settingsView_to_profileFragment, bundle)
                        Log.d("SettingsView", "Navigation action executed")
                        viewModel.onProfileNavigated()
                        Log.d("SettingsView", "onProfileNavigated called")
                    } catch (e: Exception) {
                        Log.e("SettingsView", "Navigation failed", e)
                    }
                } else {
                    Log.e("SettingsView", "Token is null, cannot navigate to ProfileFragment")
                }
            }
        }

        viewModel.navigateToResetPassword.observe(viewLifecycleOwner) { shouldNavigate ->
            Log.d("SettingsView", "navigateToResetPassword observed: $shouldNavigate")
            if (shouldNavigate) {
                try {
                    val navController = NavHostFragment.findNavController(this)
                    navController.navigate(R.id.action_settingsView_to_updateUserInfoActivity)
                    Log.d("SettingsView", "Navigated to reset password")
                    viewModel.onResetPasswordNavigated()
                } catch (e: Exception) {
                    Log.e("SettingsView", "Navigation to reset password failed", e)
                }
            }
        }

        viewModel.signOutEvent.observe(viewLifecycleOwner) { shouldSignOut ->
            Log.d("SettingsView", "signOutEvent observed: $shouldSignOut")
            if (shouldSignOut) {
                try {
                    val navController = NavHostFragment.findNavController(this)
                    navController.navigate(R.id.action_settingsView_to_loginActivity)
                    Log.d("SettingsView", "Navigated to login activity")
                    navController.popBackStack(R.id.loginActivity, false)
                    Log.d("SettingsView", "Back stack popped")
                    viewModel.onSignedOut()
                } catch (e: Exception) {
                    Log.e("SettingsView", "Navigation for sign out failed", e)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        (activity as? MainActivity)?.binding?.blurOverlay?.visibility = View.GONE
        (activity as? MainActivity)?.binding?.main?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}