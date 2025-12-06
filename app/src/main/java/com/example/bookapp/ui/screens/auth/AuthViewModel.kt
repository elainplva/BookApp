package com.example.bookapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bookapp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}


class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Private mutable state for internal use
    private val _loginState = MutableStateFlow<AuthState>(AuthState.Idle)

    // Public immutable state for UI observation
    val loginState: StateFlow<AuthState> = _loginState


    fun login(email: String, password: String) {
        // Input validation
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = AuthState.Error("Please fill all fields")
            return
        }

        // Launch coroutine for async operation
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                // Attempt login through repository
                val user = userRepository.login(email, password)
                if (user != null) {
                    _loginState.value = AuthState.Success
                } else {
                    _loginState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }


    fun register(username: String, email: String, password: String) {
        // Input validation
        if (username.isBlank() || email.isBlank() || password.isBlank()) {
            _loginState.value = AuthState.Error("Please fill all fields")
            return
        }

        // Launch coroutine for async operation
        viewModelScope.launch {
            _loginState.value = AuthState.Loading
            try {
                // Register user through repository
                userRepository.register(username, email, password)
                _loginState.value = AuthState.Success
            } catch (e: Exception) {
                _loginState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }
}


class AuthViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
