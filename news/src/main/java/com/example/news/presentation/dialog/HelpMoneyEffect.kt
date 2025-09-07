package com.example.news.presentation.dialog

sealed class HelpMoneyEffect {
    data object Dismiss : HelpMoneyEffect()
    data object RequestNotificationPermission : HelpMoneyEffect()
    data object OpenNotificationSettings : HelpMoneyEffect()
}
