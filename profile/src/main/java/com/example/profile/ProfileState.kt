package com.example.profile

sealed class ProfileState {
    data object Idle : ProfileState()
    data class Result(val friends: List<Friend>) : ProfileState()
    data object Error : ProfileState()
}
