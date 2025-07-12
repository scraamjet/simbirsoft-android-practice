package com.example.filter.presentation

import com.example.core.model.FilterCategory

sealed class FilterState {
    data object Loading : FilterState()
    data class Result(val categories: List<FilterCategory>) : FilterState()
    data object Error : FilterState()
}