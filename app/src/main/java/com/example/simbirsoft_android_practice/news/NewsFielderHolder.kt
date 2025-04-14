package com.example.simbirsoft_android_practice.news

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object NewsFlowHolder {
    private val _unreadNewsCount = MutableStateFlow(0)
    val unreadNewsCount: StateFlow<Int> = _unreadNewsCount

    fun updateUnreadCount(count: Int) {
        _unreadNewsCount.value = count
    }
}