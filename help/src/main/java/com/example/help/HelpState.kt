package com.example.help

import com.example.core.HelpCategory

sealed class HelpState {
    data object Loading : HelpState()
    data class Result(val categories: List<HelpCategory>) : HelpState()
    data object Error : HelpState()
}
