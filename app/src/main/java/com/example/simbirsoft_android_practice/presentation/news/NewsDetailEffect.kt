package com.example.simbirsoft_android_practice.presentation.news

import androidx.annotation.StringRes

sealed class NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : NewsDetailEffect()
}
