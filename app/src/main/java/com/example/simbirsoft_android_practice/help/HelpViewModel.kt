package com.example.simbirsoft_android_practice.help

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import com.example.simbirsoft_android_practice.model.HelpCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class HelpViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categories = MutableStateFlow<List<HelpCategory>?>(null)
    val categories: StateFlow<List<HelpCategory>?> = _categories.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _loading.value = true
            categoryRepository.getCategories()
                .flowOn(Dispatchers.IO)
                .map { list -> list.map(CategoryMapper::toHelpCategory) }
                .catch { e ->
                    Log.e("HelpViewModel", "Error loading categories: ${e.localizedMessage}", e)
                    _categories.value = emptyList()
                }
                .collect {
                    _categories.value = it
                    _loading.value = false
                }
        }
    }
}
