package com.example.simbirsoft_android_practice.auth

data class AuthorizationUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false
)
