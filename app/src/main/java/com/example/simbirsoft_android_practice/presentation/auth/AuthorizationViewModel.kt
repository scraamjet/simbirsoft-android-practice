package com.example.simbirsoft_android_practice.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_INPUT_LENGTH = 6

class AuthorizationViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AuthorizationState())
    val state: StateFlow<AuthorizationState> = _state.asStateFlow()

        private val _effect = MutableSharedFlow<AuthorizationEffect>()
        val effect: SharedFlow<AuthorizationEffect> = _effect.asSharedFlow()

        fun onEvent(event: AuthorizationEvent) {
            when (event) {
                is AuthorizationEvent.EmailChanged -> handleEmailChanged(event.text)
                is AuthorizationEvent.PasswordChanged -> handlePasswordChanged(event.text)
                is AuthorizationEvent.TogglePasswordVisibility -> handleTogglePasswordVisibility()
                is AuthorizationEvent.SubmitClicked -> handleSubmit()
                is AuthorizationEvent.BackClicked -> handleBack()
            }
        }

        private fun handleEmailChanged(text: String) {
            _state.update { previousState ->
                val newState = previousState.copy(email = text)
                newState.copy(isFormValid = validateForm(newState))
            }
        }

        private fun handlePasswordChanged(text: String) {
            _state.update { previousState ->
                val newState = previousState.copy(password = text)
                newState.copy(isFormValid = validateForm(newState))
            }
        }

        private fun handleTogglePasswordVisibility() {
            _state.update { previousState ->
                previousState.copy(isPasswordVisible = !previousState.isPasswordVisible)
            }
        }

        private fun handleSubmit() {
            viewModelScope.launch {
                _effect.emit(AuthorizationEffect.NavigateToHelp)
                _effect.emit(AuthorizationEffect.StartNewsService)
            }
        }

        private fun handleBack() {
            viewModelScope.launch {
                _effect.emit(AuthorizationEffect.FinishActivity)
            }
        }

        private fun validateForm(state: AuthorizationState): Boolean {
            return state.email.length >= MIN_INPUT_LENGTH &&
                state.password.length >= MIN_INPUT_LENGTH
        }
    }
