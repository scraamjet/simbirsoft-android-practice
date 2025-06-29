package com.example.help.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.help.domain.CategoriesHelpUseCase
import com.example.help.R
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

class HelpViewModel @Inject constructor(
    private val categoriesHelpUseCase: CategoriesHelpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HelpState>(HelpState.Loading)
    val state: StateFlow<HelpState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HelpEffect>()
    val effect: SharedFlow<HelpEffect> = _effect.asSharedFlow()

    init {
        onEvent(HelpEvent.Load)
    }

    fun onEvent(event: HelpEvent) {
        when (event) {
            HelpEvent.Load -> loadCategories()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoriesHelpUseCase()
                .onStart {
                    _state.value = HelpState.Loading
                }
                .catch {
                    _state.value = HelpState.Error
                    _effect.emit(HelpEffect.ShowErrorToast(R.string.help_load_error))
                }
                .collect { categoryList ->
                    _state.value = HelpState.Result(categoryList)
                }
        }
    }
}
