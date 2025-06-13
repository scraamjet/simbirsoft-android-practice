package com.example.simbirsoft_android_practice.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FilterViewModel"

class FilterViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val filterPreferenceDataStore: FilterPreferenceDataStore
) : ViewModel() {

    private val _categories = MutableStateFlow<List<FilterCategory>>(emptyList())
    val categories: StateFlow<List<FilterCategory>> = _categories.asStateFlow()

        private val _loading = MutableStateFlow(false)
        val loading: StateFlow<Boolean> = _loading.asStateFlow()

        init {
            loadCategoriesWithSelection()
        }

        private fun loadCategoriesWithSelection() {
            viewModelScope.launch {
                _loading.value = true
                combine(
                    categoryRepository.getCategories(),
                    filterPreferenceDataStore.selectedCategories,
                ) { categories, selectedIds ->
                    categories.map { category ->
                        CategoryMapper.toFilterCategory(category, selectedIds)
                    }
                }
                    .catch { exception ->
                        _categories.value = emptyList()
                        Log.e(
                            TAG,
                            "Filter categories loading exception: ${exception.localizedMessage}",
                            exception,
                        )
                    }
                    .collect { result ->
                        _categories.value = result
                        _loading.value = false
                    }
            }
        }

        fun saveSelected(ids: Set<Int>) {
            viewModelScope.launch {
                filterPreferenceDataStore.saveSelectedCategories(ids)
            }
        }
    }
