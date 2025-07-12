package com.example.simbirsoft_android_practice.presentation.filter

sealed class FilterEvent {
    data object Load : FilterEvent()
    data object OnBackClicked : FilterEvent()
    data class OnApplyClicked(val selectedIds: Set<Int>) : FilterEvent()
}
