package com.example.simbirsoft_android_practice.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.model.NewsDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsDetailViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _newsDetail = MutableStateFlow<NewsDetail?>(null)
    val newsDetail: StateFlow<NewsDetail?> = _newsDetail.asStateFlow()

    fun loadNewsDetail(newsId: Int) {
        viewModelScope.launch {
            eventRepository.getEvents(null)
                .flowOn(Dispatchers.IO)
                .map { list ->
                    list.find { it.id == newsId }?.let(NewsMapper::eventToNewsDetail)
                }
                .filterNotNull()
                .catch { e ->
                    Log.e("NewsDetailViewModel", "Error: ${e.localizedMessage}", e)
                }
                .collect {
                    _newsDetail.value = it
                }
        }
    }
}
