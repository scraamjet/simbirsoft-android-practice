package com.example.help.presentation

sealed class HelpEffect {
    data object ShowErrorToast : HelpEffect()
}

