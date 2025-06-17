package com.example.simbirsoft_android_practice.presentation.auth

sealed class AuthorizationEffect {
    data object NavigateToHelp : AuthorizationEffect()
    data object StartNewsService : AuthorizationEffect()
    data object FinishActivity : AuthorizationEffect()
}

