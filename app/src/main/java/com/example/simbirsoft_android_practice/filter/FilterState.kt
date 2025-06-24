package com.example.simbirsoft_android_practice.filter

import com.example.simbirsoft_android_practice.model.FilterCategory

sealed class FilterState {
    object Loading : FilterState()
    data class Success(val categories: List<FilterCategory>) : FilterState()
    object Error : FilterState()
}