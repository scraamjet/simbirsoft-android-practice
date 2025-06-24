package com.example.simbirsoft_android_practice.presentation.filter

sealed class FilterEffect {
    object NavigateBack : FilterEffect()
    data class ShowToast(val messageResId: Int) : FilterEffect()
}