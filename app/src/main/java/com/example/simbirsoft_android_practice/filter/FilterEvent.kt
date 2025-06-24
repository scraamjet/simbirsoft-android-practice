package com.example.simbirsoft_android_practice.filter

sealed class FilterEvent {
    data class OnApplyClicked(val selectedIds: Set<Int>) : FilterEvent()
    object OnBackClicked : FilterEvent()
}