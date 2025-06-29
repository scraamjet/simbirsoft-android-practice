package com.example.filter.presentation

import androidx.annotation.StringRes

sealed class FilterEffect {
    data object NavigateBack : FilterEffect()
    data class ShowSuccessToast(@StringRes val messageResId: Int) : FilterEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : FilterEffect()
}
