package com.example.simbirsoft_android_practice.presentation.auth

sealed class AuthorizationEvent {
    data class EmailChanged(val text: String) : AuthorizationEvent()
    data class PasswordChanged(val text: String) : AuthorizationEvent()
    data object TogglePasswordVisibility : AuthorizationEvent()
    data object SubmitClicked : AuthorizationEvent()
    data object BackClicked : AuthorizationEvent()
}
