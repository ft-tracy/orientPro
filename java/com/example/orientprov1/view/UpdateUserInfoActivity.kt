package com.example.orientprov1.view

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.orientprov1.databinding.ActivityUpdateUserInfoBinding
import com.example.orientprov1.viewmodel.UpdateUserInfoViewModel
import com.example.orientprov1.model.Result
import com.example.orientprov1.model.repository.UserRepository
import com.example.orientprov1.viewmodel.UpdateUserInfoViewModelFactory
import androidx.appcompat.app.AppCompatActivity
import com.example.orientprov1.model.api.ApiClient
import com.example.orientprov1.model.api.ApiService

class UpdateUserInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserInfoBinding
    private lateinit var viewModel: UpdateUserInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val apiService = ApiClient.createService(ApiService::class.java)
        val sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val repository = UserRepository(apiService, sharedPreferences)
        viewModel = ViewModelProvider(this, UpdateUserInfoViewModelFactory(repository))
            .get(UpdateUserInfoViewModel::class.java)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnRegister.setOnClickListener { updateUserInfo() }
    }

    private fun updateUserInfo() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.updateUserInfo(email, password, confirmPassword)
    }

    private fun observeViewModel() {
        viewModel.updateResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data, Toast.LENGTH_LONG).show()
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.btnRegister.isEnabled = !isLoading
        // You can add a ProgressBar to your layout and show/hide it here
    }
}