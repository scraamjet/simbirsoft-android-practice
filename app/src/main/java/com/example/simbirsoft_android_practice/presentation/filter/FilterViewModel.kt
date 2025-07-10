package com.example.simbirsoft_android_practice.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.domain.usecase.CategoriesFilterUseCase
import com.example.core.usecase.FilterPreferencesUseCase
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
        onEvent(FilterEvent.Load)
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoriesFilterUseCase(filterPreferencesUseCase.getSelectedCategoryIds())
                .onStart {
                    _state.value = FilterState.Loading
                }
                .catch {
                    _state.value = FilterState.Error
                    _effect.emit(FilterEffect.ShowErrorToast)
                }
                .collect { categoryList ->
                    _state.value = FilterState.Result(categoryList)
                }
        }
    }

    fun onEvent(event: FilterEvent) {
        when (event) {
            is FilterEvent.OnApplyClicked -> handleOnApplyClicked(event.selectedIds)
            FilterEvent.OnBackClicked -> handleBackClicked()
            FilterEvent.Load -> loadCategories()
        }
    }

    private fun handleOnApplyClicked(ids: Set<Int>) {
        viewModelScope.launch {
            filterPreferencesUseCase.saveSelectedCategoryIds(ids)
            _effect.emit(FilterEffect.ShowFilterSavedToast)
            _effect.emit(FilterEffect.NavigateBack)
        }
    }

    private fun handleBackClicked() {
        viewModelScope.launch {
            _effect.emit(FilterEffect.NavigateBack)
        }
    }
}
