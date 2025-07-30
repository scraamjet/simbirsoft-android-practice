package com.example.news.presentation

sealed class HelpMoneyEvent {
    data class Init(val newsId: Int, val newsTitle: String) : HelpMoneyEvent()
    data class SelectPredefinedAmount(val amount: Int) : HelpMoneyEvent()
    data class InputCustomAmount(val text: String) : HelpMoneyEvent()
    data object SendClicked : HelpMoneyEvent()
    data object PermissionGranted : HelpMoneyEvent()
    data object PermissionDenied : HelpMoneyEvent()
}
