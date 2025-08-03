package com.example.news.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.worker.DonateWorker
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class HelpMoneyViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {

    private val _state = MutableStateFlow(HelpMoneyState())
    val state: StateFlow<HelpMoneyState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HelpMoneyEffect>()
    val effect: SharedFlow<HelpMoneyEffect> = _effect.asSharedFlow()

    fun onEvent(event: HelpMoneyEvent) {
        when (event) {
            is HelpMoneyEvent.Init -> {
                _state.update { currentState ->
                    currentState.copy(newsId = event.newsId, newsTitle = event.newsTitle)
                }
            }

            is HelpMoneyEvent.SelectPredefinedAmount -> {
                _state.update { currentState ->
                    currentState.copy(selectedAmount = event.amount, inputText = "", isValid = true)
                }
            }

            is HelpMoneyEvent.InputCustomAmount -> {
                val cleanedText = event.text.filter { char -> char.isDigit() }

                val amountValue = cleanedText.toIntOrNull()
                val isValidAmount = amountValue != null && amountValue in 1..9_999_999

                _state.update { currentState ->
                    val shouldOverride = cleanedText.isNotBlank()

                    currentState.copy(
                        inputText = cleanedText,
                        selectedAmount = if (shouldOverride) null else currentState.selectedAmount,
                        isValid = if (shouldOverride) isValidAmount else currentState.selectedAmount != null
                    )
                }
            }

            is HelpMoneyEvent.OnSendClicked -> {
                viewModelScope.launch {
                    _effect.emit(HelpMoneyEffect.RequestNotificationPermission)
                }
            }

            is HelpMoneyEvent.PermissionGranted -> {
                val currentState = _state.value
                val amountToDonate =
                    currentState.inputText.toIntOrNull() ?: currentState.selectedAmount
                if (amountToDonate != null) {
                    enqueueDonateWork(currentState.newsId, currentState.newsTitle, amountToDonate)
                    viewModelScope.launch { _effect.emit(HelpMoneyEffect.Dismiss) }
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

    private fun enqueueDonateWork(newsId: Int, newsTitle: String, amount: Int) {
        val data = workDataOf(
            "news_id" to newsId,
            "news_title" to newsTitle,
            "amount" to amount
        )

        val constraints = Constraints.Builder()
            .build()

        val request = OneTimeWorkRequestBuilder<DonateWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        workManager.enqueue(request)
    }
}