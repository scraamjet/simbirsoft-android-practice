package com.example.simbirsoft_android_practice.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.domain.usecase.NewsDetailUseCase
import com.example.simbirsoft_android_practice.domain.model.NewsDetail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NewsDetailViewModel"

class NewsDetailViewModel @Inject constructor(
    private val newsDetailUseCase: NewsDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NewsDetailState?>(null)
    val state: StateFlow<NewsDetailState?> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NewsDetailEffect>()
    val effect: SharedFlow<NewsDetailEffect> = _effect.asSharedFlow()

    fun onEvent(event: NewsDetailEvent) {
        when (event) {
            is NewsDetailEvent.LoadNewsDetail -> loadNewsDetail(newsId = event.newsId)
        }
    }

    private fun loadNewsDetail(newsId: Int) {
        viewModelScope.launch {
            try {
                val newsDetail: NewsDetail? = newsDetailUseCase.execute(newsId = newsId)
                if (newsDetail != null) {
                    _state.value = NewsDetailState.Result(newsDetail = newsDetail)
                } else {
                    _state.value = NewsDetailState.Error(message = "Новость не найдена")
                }
            } catch (exception: Exception) {
                _state.value = NewsDetailState.Error(
                    message = exception.localizedMessage ?: "Неизвестная ошибка"
                )
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(NewsDetailEffect.NavigateBack)
        }
    }
}

