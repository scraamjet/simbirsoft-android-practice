package com.example.simbirsoft_android_practice.presentation.help

sealed class HelpEffect {
    data object ShowErrorToast : HelpEffect()
}

