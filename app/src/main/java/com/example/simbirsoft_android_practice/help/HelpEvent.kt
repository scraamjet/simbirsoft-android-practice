package com.example.simbirsoft_android_practice.help

sealed interface HelpEvent {
    data object Loaded : HelpEvent
}