package com.example.simbirsoft_android_practice.auth

import androidx.annotation.DrawableRes
import com.example.simbirsoft_android_practice.R

enum class VisibilityPassword(val isVisible: Boolean, @DrawableRes val iconRes: Int) {
    OPEN(true, R.drawable.ic_open_password),
    HIDE(false, R.drawable.ic_hide_password);

    fun toggle(): VisibilityPassword {
        return if (this == HIDE) {
            OPEN
        } else {
            HIDE
        }
    }
}