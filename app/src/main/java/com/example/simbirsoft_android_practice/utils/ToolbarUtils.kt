package com.example.simbirsoft_android_practice.utils

import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout

private const val SCROLL_FLAG_NONE = 0

fun Toolbar.updateScrollFlags(isListEmpty: Boolean) {
    (layoutParams as AppBarLayout.LayoutParams).apply {
        scrollFlags = if (isListEmpty) {
            SCROLL_FLAG_NONE
        } else {
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }
    }
}



