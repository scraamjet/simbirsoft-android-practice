package com.example.simbirsoft_android_practice.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.CategoriesFilterUseCase
import com.example.simbirsoft_android_practice.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.model.FilterCategory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


class FilterViewModel @Inject constructor(
    private val categoriesFilterUseCase: CategoriesFilterUseCase,
    private val filterPreferencesUseCase: FilterPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<FilterState>(FilterState.Loading)
    val state: StateFlow<FilterState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<FilterEffect>()
    val effect: SharedFlow<FilterEffect> = _effect.asSharedFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoriesFilterUseCase(filterPreferencesUseCase.getSelectedCategoryIds())
                .onStart {
                    _state.value = FilterState.Loading
                }
                .catch { throwable ->
                    _state.value = FilterState.Error
                }
                .collect { categoryList ->
                    _state.value = FilterState.Success(categoryList)
                }
        }
    }

    fun onEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.OnApplyClicked -> {
                saveSelected(event.selectedIds)
            }

            FilterEvent.OnBackClicked -> {
                viewModelScope.launch {
                    _effect.emit(FilterEffect.NavigateBack)
                }
            }
        }
    }

    private fun saveSelected(ids: Set<Int>) {
        viewModelScope.launch {
            filterPreferencesUseCase.saveSelectedCategoryIds(ids)
            _effect.emit(FilterEffect.ShowToast(R.string.filter_saved_toast))
            _effect.emit(FilterEffect.NavigateBack)
        }
    }
}

