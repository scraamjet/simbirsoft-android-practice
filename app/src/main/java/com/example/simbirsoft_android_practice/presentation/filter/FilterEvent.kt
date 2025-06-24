package com.example.simbirsoft_android_practice.presentation.filter

sealed class FilterEvent {
    data class OnApplyClicked(val selectedIds: Set<Int>) : FilterEvent()
    object OnBackClicked : FilterEvent()
}