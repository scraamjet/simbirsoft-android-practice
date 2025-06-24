package com.example.simbirsoft_android_practice.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.NewsUseCase
import com.example.simbirsoft_android_practice.filter.FilterPreferenceDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NewsViewModel"

class NewsViewModel @Inject constructor(
    private val filterPreferencesUseCase: FilterPreferencesUseCase,
    private val getNewsUseCase: NewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    init {
        observeSelectedCategories()
    }

    private fun observeSelectedCategories() {
        viewModelScope.launch {
            filterPreferencesUseCase.getSelectedCategoryIds()
                .distinctUntilChanged()
                .collectLatest { selectedCategories ->
                    loadNews(selectedCategories)
                }
        }
    }

    private suspend fun loadNews(selectedCategories: Set<Int>) {
        _uiState.value = NewsUiState.Loading
        try {
            val filteredNews = getNewsUseCase.execute(selectedCategories)
            _uiState.value = if (filteredNews.isEmpty()) {
                NewsUiState.NoResults
            } else {
                NewsUiState.Results(filteredNews)
            }
        } catch (e: Exception) {
            _uiState.value = NewsUiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }
}

