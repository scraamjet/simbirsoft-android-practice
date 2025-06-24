package com.example.simbirsoft_android_practice.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simbirsoft_android_practice.CategoriesHelpUseCase
import com.example.simbirsoft_android_practice.model.HelpCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HelpViewModel"

class HelpViewModel @Inject constructor(
    private val categoriesHelpUseCase: CategoriesHelpUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<HelpCategory>>(emptyList())
    val categories: StateFlow<List<HelpCategory>> = _categories.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true
            categoriesHelpUseCase()
                .catch { _categories.value = emptyList() }
                .collect {
                    _categories.value = it
                    _loading.value = false
                }
        }
    }
}
