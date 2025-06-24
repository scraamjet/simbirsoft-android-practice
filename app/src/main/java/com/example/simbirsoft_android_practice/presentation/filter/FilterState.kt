package com.example.simbirsoft_android_practice.presentation.filter

import com.example.simbirsoft_android_practice.domain.model.FilterCategory

sealed class FilterState {
    object Loading : FilterState()
    data class Success(val categories: List<FilterCategory>) : FilterState()
    object Error : FilterState()
}