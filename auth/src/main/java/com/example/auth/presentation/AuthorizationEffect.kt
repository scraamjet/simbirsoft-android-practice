package com.example.auth.presentation

sealed class AuthorizationEffect {
    data object NavigateToHelp : AuthorizationEffect()
    data object StartNewsService : AuthorizationEffect()
    data object FinishActivity : AuthorizationEffect()
}

