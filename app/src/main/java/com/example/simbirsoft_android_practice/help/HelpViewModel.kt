package com.example.simbirsoft_android_practice.help

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import com.example.simbirsoft_android_practice.model.HelpCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "HelpViewModel"

class HelpViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    val categories: StateFlow<List<HelpCategory>> =
        categoryRepository.getCategories()
            .onStart { _loading.value = true }
            .flowOn(Dispatchers.IO)
            .map { categoryList -> categoryList.map(CategoryMapper::toHelpCategory) }
            .catch { exception: Throwable ->
                Log.e(
                    TAG,
                    "Help categories loading exception: ${exception.localizedMessage}",
                    exception,
                )
            }
            .onEach { _ -> _loading.value = false }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
}
