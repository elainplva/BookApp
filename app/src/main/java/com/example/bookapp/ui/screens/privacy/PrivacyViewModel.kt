package com.example.bookapp.ui.screens.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class PrivacyViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            userRepository.deleteCurrentUser()
        }
    }
}

class PrivacyViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrivacyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PrivacyViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}