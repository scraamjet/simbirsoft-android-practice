package com.example.simbirsoft_android_practice.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class FilterViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val filterPreferences: FilterPreferences
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
                filterPreferences.selectedCategories
            ) { categories, selectedIds ->
                categories.map { category ->
                    CategoryMapper.toFilterCategory(category, selectedIds)
                }
            }.collect { result ->
                _categories.value = result
                _loading.value = false
            }
        }
    }

    fun saveSelected(ids: Set<Int>) {
        viewModelScope.launch {
            filterPreferences.saveSelectedCategories(ids)
        }
    }
}
