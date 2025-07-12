package com.example.help.presentation

import com.example.core.model.HelpCategory

sealed class HelpState {
    data object Loading : HelpState()
    data class Result(val categories: List<HelpCategory>) : HelpState()
    data object Error : HelpState()
}
