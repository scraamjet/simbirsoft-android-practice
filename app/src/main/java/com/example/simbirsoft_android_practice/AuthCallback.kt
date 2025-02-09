package com.example.simbirsoft_android_practice

interface AuthCallback {
    fun authSuccess(user: User)
    fun authFailed(reason: String)
}