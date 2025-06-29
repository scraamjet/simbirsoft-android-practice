package com.example.help

import androidx.annotation.StringRes

sealed class HelpEffect {
    data class ShowErrorToast(@StringRes val messageResId: Int) : HelpEffect()
}
