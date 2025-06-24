package com.example.simbirsoft_android_practice.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.domain.usecase.CategoriesHelpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


class HelpViewModel @Inject constructor(
    private val categoriesHelpUseCase: CategoriesHelpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HelpState>(HelpState.Loading)
    val state: StateFlow<HelpState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            categoriesHelpUseCase()
                .onStart {
                    _state.value = HelpState.Loading
                }
                .catch {
                    _state.value = HelpState.Error
                }
                .collect { categoryList ->
                    _state.value = HelpState.Success(categoryList)
                }
        }
    }
}
