package com.example.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <T> MutableStateFlow<T>.updateState(
    crossinline transform: (T) -> T
) where T : Any {
    this.update { currentState -> transform(currentState) }
}