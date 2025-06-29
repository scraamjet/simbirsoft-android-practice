package com.example.help

sealed interface HelpEvent {
    data object Load : HelpEvent
}
