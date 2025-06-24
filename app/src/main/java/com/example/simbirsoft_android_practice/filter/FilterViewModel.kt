package com.example.simbirsoft_android_practice.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.CategoriesFilterUseCase
import com.example.simbirsoft_android_practice.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FilterViewModel"

class FilterViewModel @Inject constructor(
    private val categoriesFilterUseCase: CategoriesFilterUseCase,
    private val filterPreferencesUseCase: FilterPreferencesUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<FilterCategory>>(emptyList())
    val categories: StateFlow<List<FilterCategory>> = _categories.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true
            categoriesFilterUseCase(filterPreferencesUseCase.getSelectedCategoryIds())
                .catch {
                    _categories.value = emptyList()
                    _loading.value = false
                }
                .collect {
                    _categories.value = it
                    _loading.value = false
                }
        }
    }

    fun saveSelected(ids: Set<Int>) {
        viewModelScope.launch {
            filterPreferencesUseCase.saveSelectedCategoryIds(ids)
        }
    }
}

