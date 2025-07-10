package com.example.filter.presentation

sealed class FilterEffect {
    data object NavigateBack : FilterEffect()
    data object ShowFilterSavedToast : FilterEffect()
    data object ShowErrorToast : FilterEffect()
}

