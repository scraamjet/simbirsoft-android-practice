package com.example.news.presentation

import com.example.news.DonateAmount

sealed class HelpMoneyEvent {
    data class Init(val newsId: Int, val newsTitle: String) : HelpMoneyEvent()
    data class SelectPredefinedAmount(val amount: DonateAmount) : HelpMoneyEvent()
    data class InputCustomAmount(val text: String) : HelpMoneyEvent()
    data object OnSendClicked : HelpMoneyEvent()
    data object PermissionGranted : HelpMoneyEvent()
    data class PermissionDenied(val shouldShowRationale: Boolean) : HelpMoneyEvent()
    data object OnCancelClicked : HelpMoneyEvent()
}
