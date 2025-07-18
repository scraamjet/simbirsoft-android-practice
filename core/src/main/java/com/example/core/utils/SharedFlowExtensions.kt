package com.example.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun <T> SharedFlow<T>.collectAsEffect(): T? {
    var effect by remember { mutableStateOf<T?>(null) }
    LaunchedEffect(Unit) {
        collect { newEffect -> effect = newEffect }
    }
    return effect
}