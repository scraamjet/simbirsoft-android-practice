package com.example.simbirsoft_android_practice.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.NewsUseCase
import com.example.simbirsoft_android_practice.model.NewsItem
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

private const val TAG = "NewsViewModel"

class NewsViewModel @Inject constructor(
    private val filterPreferencesUseCase: FilterPreferencesUseCase,
    private val newsUseCase: NewsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<NewsEffect>()
    val effect: SharedFlow<NewsEffect> = _effect.asSharedFlow()

    init {
        onEvent(NewsEvent.LoadNews)
    }

    fun onEvent(event: NewsEvent) {
        when (event) {
            is NewsEvent.LoadNews -> observeSelectedCategories()
            is NewsEvent.NewsClicked -> handleNewsClicked(newsId = event.newsId)
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
        _uiState.value = NewsUiState.Loading
        try {
            val filteredNews: List<NewsItem> = newsUseCase.execute(selectedCategories)
            _uiState.value = if (filteredNews.isEmpty()) {
                NewsUiState.NoResults
            } else {
                NewsUiState.Results(newsList = filteredNews)
            }
        } catch (exception: Exception) {
            _uiState.value = NewsUiState.Error(
                message = exception.localizedMessage ?: "Unknown error"
            )
        }
    }

    private fun handleNewsClicked(newsId: Int) {
        viewModelScope.launch {
            _effect.emit(NewsEffect.NavigateToNewsDetail(newsId = newsId))
        }
    }
}

