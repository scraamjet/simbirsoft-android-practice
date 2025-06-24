package com.example.simbirsoft_android_practice.presentation.help

sealed interface HelpEvent {
    data object Loaded : HelpEvent
}