package com.example.news.presentation.newsdetail

import androidx.annotation.StringRes

sealed class NewsDetailEffect {
    data object NavigateBack : NewsDetailEffect()
    data class ShowErrorToast(@StringRes val messageResId: Int) : NewsDetailEffect()
}
