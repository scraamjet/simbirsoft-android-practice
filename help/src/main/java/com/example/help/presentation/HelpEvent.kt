package com.example.help.presentation

sealed interface HelpEvent {
    data object Load : HelpEvent
}
