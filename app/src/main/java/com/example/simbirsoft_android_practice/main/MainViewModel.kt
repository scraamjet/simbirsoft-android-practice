package com.example.simbirsoft_android_practice.main

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.model.NewsItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val filterPreferences: FilterPreferences
) : ViewModel() {

    private val _bottomNavVisible = MutableStateFlow(true)
    val bottomNavigationVisible: StateFlow<Boolean> = _bottomNavVisible.asStateFlow()

    private val _readNewsIds = MutableStateFlow<Set<Int>>(emptySet())
    val readNewsIds: StateFlow<Set<Int>> = _readNewsIds.asStateFlow()

    private val _badgeFlow = MutableStateFlow(0)
    val badgeFlow: StateFlow<Int> = _badgeFlow.asStateFlow()

    fun updateReadNews(newsId: Int) {
        _readNewsIds.update { it + newsId }
    }

    fun updateBadge(newsItems: List<NewsItem>) {
        val unreadCount = newsItems.count { it.id !in _readNewsIds.value }
        _badgeFlow.value = unreadCount
    }

    fun getSelectedCategories(): Set<Int> = filterPreferences.getSelectedCategories()

    fun showBottomNavigation() {
        _bottomNavVisible.value = true
    }

    fun hideBottomNavigation() {
        _bottomNavVisible.value = false
    }
}

