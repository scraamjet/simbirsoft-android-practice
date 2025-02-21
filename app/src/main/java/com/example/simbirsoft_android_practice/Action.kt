package com.example.simbirsoft_android_practice

sealed class Action {
    object Registration : Action()
    data class Login(val user: User) : Action()
    object Logout : Action()
}