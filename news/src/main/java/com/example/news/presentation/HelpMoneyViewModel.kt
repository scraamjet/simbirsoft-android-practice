package com.example.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.updateState
import com.example.news.DonateUseCase
import com.example.news.utils.withCustomAmount
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class HelpMoneyViewModel @Inject constructor(
    private val donateUseCase: DonateUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HelpMoneyState())
    val state: StateFlow<HelpMoneyState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HelpMoneyEffect>()
    val effect: SharedFlow<HelpMoneyEffect> = _effect.asSharedFlow()

    fun onEvent(event: HelpMoneyEvent) {
        when (event) {
            is HelpMoneyEvent.Init -> {
                _state.updateState { state ->
                    state.copy(newsId = event.newsId, newsTitle = event.newsTitle)
                }
            }

            is HelpMoneyEvent.SelectPredefinedAmount -> {
                _state.updateState { state ->
                    state.copy(
                        selectedAmount = event.amount,
                        inputText = "",
                        isValid = true
                    )
                }
            }

            is HelpMoneyEvent.InputCustomAmount -> {
                _state.updateState { state ->
                    state.withCustomAmount(event.text)
                }
            }

            is HelpMoneyEvent.OnSendClicked -> {
                viewModelScope.launch {
                    _effect.emit(HelpMoneyEffect.RequestNotificationPermission)
                }
            }

            is HelpMoneyEvent.PermissionGranted -> {
                var amountToDonate: Int? = null

                _state.updateState { state ->
                    val inputAmount = state.inputText.toIntOrNull()
                    amountToDonate = inputAmount ?: state.selectedAmount

                    state.copy(
                        inputText = "",
                        selectedAmount = amountToDonate,
                        isValid = false
                    )
                }

                amountToDonate?.let { amount ->
                    val currentState = _state.value
                    donateUseCase.donate(currentState.newsId, currentState.newsTitle, amount)
                    viewModelScope.launch {
                        _effect.emit(HelpMoneyEffect.Dismiss)
                    }
                }
            }

            is HelpMoneyEvent.PermissionDenied -> {
                viewModelScope.launch {
                    _effect.emit(HelpMoneyEffect.ShowPermissionDeniedMessage)
                }
            }

            is HelpMoneyEvent.OnCancelClicked -> {
                viewModelScope.launch {
                    _effect.emit(HelpMoneyEffect.Dismiss)
                }
            }
        }
    }
}