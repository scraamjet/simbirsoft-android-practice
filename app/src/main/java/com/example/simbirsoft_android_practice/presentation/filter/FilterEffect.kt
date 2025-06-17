package com.example.simbirsoft_android_practice.presentation.filter

import androidx.annotation.StringRes

sealed class FilterEffect {
    data object NavigateBack : FilterEffect()
    data class ShowSuccessToast(@StringRes val messageResId: Int) : FilterEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : FilterEffect()
}
