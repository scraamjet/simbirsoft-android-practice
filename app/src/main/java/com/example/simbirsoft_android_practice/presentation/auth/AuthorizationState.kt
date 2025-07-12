package com.example.simbirsoft_android_practice.presentation.auth

data class AuthorizationState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isFormValid: Boolean = false,
)
