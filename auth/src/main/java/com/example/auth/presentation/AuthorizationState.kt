package com.example.auth.presentation

data class AuthorizationState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
)
