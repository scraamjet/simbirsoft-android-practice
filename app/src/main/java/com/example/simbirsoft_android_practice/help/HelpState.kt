package com.example.simbirsoft_android_practice.help

import com.example.simbirsoft_android_practice.domain.model.HelpCategory

sealed class HelpState {
    object Loading : HelpState()
    data class Success(val categories: List<HelpCategory>) : HelpState()
    object Error : HelpState()
}
