package com.example.core.main

data class MainState(
    val isBottomNavigationVisible: Boolean = true,
    val badgeCount: Int = 0,
    val readNewsIds: Set<Int> = emptySet(),
)
