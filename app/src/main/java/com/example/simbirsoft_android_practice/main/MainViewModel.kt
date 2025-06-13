package com.example.simbirsoft_android_practice.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.filter.FilterPreferenceDataStore
import com.example.simbirsoft_android_practice.model.NewsItem
import com.example.simbirsoft_android_practice.news.NewsMapper
import com.example.simbirsoft_android_practice.news.NewsPreferences
import com.example.simbirsoft_android_practice.news.NewsService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

class MainViewModel @Inject constructor(
    filterPreferenceDataStore: FilterPreferenceDataStore,
    private val newsPreferences: NewsPreferences
) : ViewModel() {

    private val _bottomNavVisible = MutableStateFlow(true)
    val bottomNavigationVisible: StateFlow<Boolean> = _bottomNavVisible.asStateFlow()

    private val _readNewsIds =
        MutableStateFlow(newsPreferences.getReadNewsIds())

    private val _badgeFlow = MutableStateFlow(0)
    val badgeFlow: StateFlow<Int> = _badgeFlow.asStateFlow()

    private val selectedCategories: Flow<Set<Int>> = filterPreferenceDataStore.selectedCategories

    private var serviceJob: Job? = null

    fun startNewsUpdates(service: NewsService) {
        serviceJob?.cancel()
        serviceJob = viewModelScope.launch {
            selectedCategories
                .distinctUntilChanged()
                .collectLatest { selected ->
                    service.loadNews()
                        .catch { exception ->
                            Log.e(
                                TAG,
                                "News Service loading exception: ${exception.localizedMessage}",
                                exception
                            )
                        }
                        .collect { events ->
                            val filtered = events
                                .filter { it.categoryIds.any { id -> id in selected } }
                                .map(NewsMapper::eventToNewsItem)

                            updateBadge(filtered)
                        }
                }
        }
    }

    fun updateReadNews(newsId: Int) {
        _readNewsIds.update { current ->
            if (newsId !in current) {
                val updated = current + newsId
                newsPreferences.markNewsAsReadAndSelected(newsId)
                updated
            } else {
                current
            }
        }
    }

    fun updateBadge(newsItems: List<NewsItem>) {
        val unreadCount = newsItems.count { newsItem -> newsItem.id !in _readNewsIds.value }
        _badgeFlow.value = unreadCount
    }

    fun setBottomNavigationVisible(visible: Boolean) {
        _bottomNavVisible.value = visible
    }
}


