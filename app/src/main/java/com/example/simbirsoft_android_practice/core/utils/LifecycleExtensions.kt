package com.example.simbirsoft_android_practice.core.utils

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.launchInLifecycle(
    repeatOn: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(repeatOn, action)
    }
}

fun ComponentActivity.launchInLifecycle(
    repeatOn: Lifecycle.State,
    action: suspend CoroutineScope.() -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(repeatOn, action)
    }
}