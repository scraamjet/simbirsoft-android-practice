package com.example.filter.presentation

sealed class FilterEvent {
    data object Load : FilterEvent()
    data object OnBackClicked : FilterEvent()
    data class OnApplyClicked(val selectedIds: Set<Int>) : FilterEvent()
}
