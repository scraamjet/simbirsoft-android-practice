package com.example.simbirsoft_android_practice.presentation.help

import androidx.annotation.StringRes

sealed class HelpEffect {
    data class ShowErrorToast(@StringRes val messageResId: Int) : HelpEffect()
}
