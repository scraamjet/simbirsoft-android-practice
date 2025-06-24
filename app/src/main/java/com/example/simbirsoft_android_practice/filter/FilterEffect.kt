package com.example.simbirsoft_android_practice.filter

sealed class FilterEffect {
    object NavigateBack : FilterEffect()
    data class ShowToast(val messageResId: Int) : FilterEffect()
}