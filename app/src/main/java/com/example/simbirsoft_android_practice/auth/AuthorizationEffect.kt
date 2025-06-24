package com.example.simbirsoft_android_practice.auth

sealed class AuthorizationEffect {
    data object NavigateToHelp : AuthorizationEffect()
    data object FinishActivity : AuthorizationEffect()
}
