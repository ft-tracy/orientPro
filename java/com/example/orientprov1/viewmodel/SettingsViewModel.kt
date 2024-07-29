package com.example.orientprov1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orientprov1.model.repository.DataStoreRepository
import com.example.orientprov1.model.repository.UserRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _navigateToProfile = MutableLiveData<Boolean>()
    val navigateToProfile: LiveData<Boolean> = _navigateToProfile

    private val _navigateToResetPassword = MutableLiveData<Boolean>()
    val navigateToResetPassword: LiveData<Boolean> = _navigateToResetPassword

    private val _signOutEvent = MutableLiveData<Boolean>()
    val signOutEvent: LiveData<Boolean> = _signOutEvent

    fun onProfileViewClicked() {
        Log.d("SettingsViewModel", "onProfileViewClicked called")
        _navigateToProfile.value = true
        Log.d("SettingsViewModel", "navigateToProfile set to true")
    }

    fun onProfileNavigated() {
        Log.d("SettingsViewModel", "onProfileNavigated: Setting navigateToProfile to false")
        _navigateToProfile.value = false
    }

    fun onDarkModeChanged(isEnabled: Boolean) {
        // Implement dark mode logic here
    }

    fun onSignOutClicked() {
        viewModelScope.launch {
            userRepository.clearAuthToken()
            dataStoreRepository.clearUserData()
            _signOutEvent.value = true
        }
    }

    fun onSignedOut() {
        _signOutEvent.value = false
    }

    fun onResetPasswordClicked() {
        _navigateToResetPassword.value = true
    }

    fun onResetPasswordNavigated() {
        _navigateToResetPassword.value = false
    }

    fun onRateAppClicked() {
        // Implement rate app logic
    }

    fun onPrivacyPolicyClicked() {
        // Implement privacy policy logic
    }

    fun onTermsOfServiceClicked() {
        // Implement terms of service logic
    }
}