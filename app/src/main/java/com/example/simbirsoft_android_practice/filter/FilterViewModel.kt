package com.example.simbirsoft_android_practice.filter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.model.Category
import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FilterViewModel"

class FilterViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val filterPreferences: FilterPreferences
) : ViewModel() {

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    val categories: StateFlow<List<FilterCategory>> =
        combine(
            categoryRepository.getCategories(),
            filterPreferences.selectedCategories
        ) { categoryList: List<Category>, selectedIds: Set<Int> ->
            categoryList.map { category: Category ->
                CategoryMapper.toFilterCategory(
                    category = category,
                    selectedIds = selectedIds
                )
            }
        }
            .onStart { _loading.value = true }
            .catch { exception: Throwable ->
                Log.e(
                    TAG,
                    "Filter categories loading exception: ${exception.localizedMessage}",
                    exception
                )
            }
            .onEach { _ -> _loading.value = false }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    fun saveSelected(ids: Set<Int>) {
        viewModelScope.launch {
            filterPreferences.saveSelectedCategories(ids)
        }
    }
}
