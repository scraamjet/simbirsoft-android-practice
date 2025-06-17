package com.example.simbirsoft_android_practice.presentation.filter

import com.example.simbirsoft_android_practice.domain.model.FilterCategory

sealed class FilterState {
    data object Loading : FilterState()
    data class Result(val categories: List<FilterCategory>) : FilterState()
    data object Error : FilterState()
}