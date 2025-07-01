package com.example.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.NewsBadgeCountUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsViewModel @Inject constructor(
    private val filterPreferencesUseCase: FilterPreferencesUseCase,
    private val newsUseCase: NewsUseCase,
    private val newsBadgeCountUseCase: NewsBadgeCountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsState>(NewsState.Loading)
    val uiState: StateFlow<NewsState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<NewsEffect>()
    val effect: SharedFlow<NewsEffect> = _effect.asSharedFlow()

    init {
        onEvent(NewsEvent.LoadNews)
    }

    fun onEvent(event: NewsEvent) {
        when (event) {
            is NewsEvent.LoadNews -> observeSelectedCategories()
            is NewsEvent.NewsClicked -> handleNewsClicked(newsId = event.newsId)
            is NewsEvent.FiltersClicked -> handleFiltersClicked()
        }
    }

    private fun observeSelectedCategories() {
        viewModelScope.launch {
            filterPreferencesUseCase.getSelectedCategoryIds()
                .distinctUntilChanged()
                .collectLatest { selectedCategories ->
                    loadNews(selectedCategories = selectedCategories)
                }
        }
    }

    private suspend fun loadNews(selectedCategories: Set<Int>) {
        Log.d("NewsViewModel", "Loading news for: $selectedCategories")
        _uiState.value = NewsState.Loading

        try {
            val filteredNews = newsUseCase.execute(selectedCategories)
            Log.d("NewsViewModel", "Loaded news: ${filteredNews.size}")

            newsBadgeCountUseCase.updateNews(newsItems = filteredNews)
            Log.d("NewsViewModel", "Badge count updated, count: ${filteredNews.size}")

            _uiState.value = if (filteredNews.isEmpty()) {
                NewsState.NoResults
            } else {
                NewsState.Results(newsList = filteredNews)
            }
        } catch (exception: Exception) {
            Log.e("NewsViewModel", "Error loading news", exception)
            _uiState.value = NewsState.Error
            _effect.emit(NewsEffect.ShowErrorToast(R.string.news_load_error))
        }
    }


    private fun handleFiltersClicked() {
        viewModelScope.launch {
            _effect.emit(NewsEffect.NavigateToFilter)
        }
    }

    private fun handleNewsClicked(newsId: Int) {
        viewModelScope.launch {
            newsBadgeCountUseCase.markNewsAsRead(newsId)
            _effect.emit(NewsEffect.NavigateToNewsDetail(newsId = newsId))
        }
    }
}
