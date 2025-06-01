package com.example.simbirsoft_android_practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

private const val MIN_INPUT_LENGTH = 6

class AuthorizationViewModel : ViewModel() {

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    val isFormValid: StateFlow<Boolean> = combine(_email, _password) { email, password ->
        email.length >= MIN_INPUT_LENGTH && password.length >= MIN_INPUT_LENGTH
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun onEmailChanged(text: String) {
        _email.value = text
    }

    fun onPasswordChanged(text: String) {
        _password.value = text
    }
}
