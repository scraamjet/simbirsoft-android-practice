package com.example.simbirsoft_android_practice.presentation.help

import com.example.simbirsoft_android_practice.domain.model.HelpCategory

sealed class HelpState {
    data object Loading : HelpState()
    data class Result(val categories: List<HelpCategory>) : HelpState()
    data object Error : HelpState()
}
