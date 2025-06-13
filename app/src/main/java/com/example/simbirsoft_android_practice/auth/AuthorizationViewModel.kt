package com.example.simbirsoft_android_practice.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val MIN_INPUT_LENGTH = 6

class AuthorizationViewModel
    @Inject
    constructor() : ViewModel() {
        private val _email = MutableStateFlow("")

        private val _password = MutableStateFlow("")

        private val _isPasswordVisible = MutableStateFlow(false)
        val isPasswordVisible: StateFlow<Boolean> = _isPasswordVisible.asStateFlow()

        private val _isFormValid =
            combine(_email, _password) { email, password ->
                email.length >= MIN_INPUT_LENGTH && password.length >= MIN_INPUT_LENGTH
            }.stateIn(viewModelScope, SharingStarted.Lazily, false)

        val isFormValid: StateFlow<Boolean> = _isFormValid

        fun onEmailChanged(text: String) {
            _email.value = text
        }

        fun onPasswordChanged(text: String) {
            _password.value = text
        }

        fun togglePasswordVisibility() {
            _isPasswordVisible.value = !_isPasswordVisible.value
        }
    }
