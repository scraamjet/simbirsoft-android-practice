package com.example.news.presentation

sealed class HelpMoneyEffect {
    data object Dismiss : HelpMoneyEffect()
    data object RequestNotificationPermission : HelpMoneyEffect()
    data object OpenNotificationSettings : HelpMoneyEffect()
}
