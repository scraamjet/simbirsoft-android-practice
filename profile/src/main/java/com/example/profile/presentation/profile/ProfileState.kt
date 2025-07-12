package com.example.profile.presentation.profile

import com.example.profile.presentation.model.Friend

sealed class ProfileState {
    data object Idle : ProfileState()
    data class Result(val friends: List<Friend>) : ProfileState()
    data object Error : ProfileState()
}
