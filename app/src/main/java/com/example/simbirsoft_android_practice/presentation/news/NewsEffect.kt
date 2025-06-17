package com.example.simbirsoft_android_practice.presentation.news

import androidx.annotation.StringRes


sealed class NewsEffect {
    data class NavigateToNewsDetail(val newsId: Int) : NewsEffect()
    data object NavigateToFilter : NewsEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : NewsEffect()
}

