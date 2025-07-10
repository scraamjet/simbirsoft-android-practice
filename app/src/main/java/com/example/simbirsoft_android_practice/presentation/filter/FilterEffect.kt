package com.example.simbirsoft_android_practice.presentation.filter

sealed class FilterEffect {
    data object NavigateBack : FilterEffect()
    data object ShowFilterSavedToast : FilterEffect()
    data object ShowErrorToast : FilterEffect()
}

