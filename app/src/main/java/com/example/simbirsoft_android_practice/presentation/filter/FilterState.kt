package com.example.simbirsoft_android_practice.presentation.filter

import com.example.core.FilterCategory

sealed class FilterState {
    data object Loading : FilterState()
    data class Result(val categories: List<FilterCategory>) : FilterState()
    data object Error : FilterState()
}