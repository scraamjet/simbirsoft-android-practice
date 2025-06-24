package com.example.simbirsoft_android_practice.presentation.help

import com.example.simbirsoft_android_practice.domain.model.HelpCategory

sealed class HelpState {
    object Loading : HelpState()
    data class Success(val categories: List<HelpCategory>) : HelpState()
    object Error : HelpState()
}
