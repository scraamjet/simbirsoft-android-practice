package com.example.simbirsoft_android_practice.presentation.profile

import com.example.simbirsoft_android_practice.domain.model.Friend

sealed class ProfileState {
    data object Idle : ProfileState()
    data class Result(val friends: List<Friend>) : ProfileState()
    data object Error : ProfileState()
}
