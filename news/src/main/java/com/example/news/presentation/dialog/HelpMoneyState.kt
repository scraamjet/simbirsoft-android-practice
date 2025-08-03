package com.example.news.presentation.dialog

data class HelpMoneyState(
    val newsId: Int = 0,
    val newsTitle: String = "",
    val selectedAmount: Int? = null,
    val inputText: String = "",
    val isValid: Boolean = false
)
