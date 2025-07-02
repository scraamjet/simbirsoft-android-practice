package com.example.news.presentation.newsdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.R
import com.example.core.model.NewsDetail
import com.example.news.domain.usecase.NewsDetailUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsDetailViewModel @Inject constructor(
    private val newsDetailUseCase: NewsDetailUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<NewsDetailState>(NewsDetailState.Idle)
    val state: StateFlow<NewsDetailState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<NewsDetailEffect>()
    val effect: SharedFlow<NewsDetailEffect> = _effect.asSharedFlow()

    fun onEvent(event: NewsDetailEvent) {
        when (event) {
            is NewsDetailEvent.Load -> loadNewsDetail(newsId = event.newsId)
        }
    }

    private fun loadNewsDetail(newsId: Int) {
        viewModelScope.launch {
            try {
                val newsDetail: NewsDetail? = newsDetailUseCase.execute(newsId = newsId)
                if (newsDetail != null) {
                    _state.value = NewsDetailState.Result(newsDetail = newsDetail)
                } else {
                    emitErrorState()
                }
            } catch (exception: Exception) {
                emitErrorState()
            }
        }
    }

    private suspend fun emitErrorState() {
        _state.value = NewsDetailState.Error
        _effect.emit(NewsDetailEffect.ShowErrorToast(R.string.news_detail_load_error))
    }

    fun handleOnBackClicked() {
        viewModelScope.launch {
            _effect.emit(NewsDetailEffect.NavigateBack)
        }
    }
}
