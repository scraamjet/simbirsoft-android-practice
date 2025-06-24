package com.example.simbirsoft_android_practice.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.NewsDetailUseCase
import com.example.simbirsoft_android_practice.model.NewsDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "NewsDetailViewModel"

class NewsDetailViewModel @Inject constructor(
    private val newsDetailUseCase: NewsDetailUseCase
) : ViewModel() {

    private val _newsDetail = MutableStateFlow<NewsDetail?>(null)
    val newsDetail: StateFlow<NewsDetail?> = _newsDetail.asStateFlow()

    fun loadNewsDetail(newsId: Int) {
        viewModelScope.launch {
            try {
                val detail = newsDetailUseCase.execute(newsId)
                _newsDetail.value = detail
            } catch (e: Exception) {
                Log.e(TAG, "News detail loading error: ${e.message}", e)
            }
        }
    }
}
