package com.example.simbirsoft_android_practice.profile

import com.example.simbirsoft_android_practice.model.Friend

sealed class ProfileState {
    data class Success(val friends: List<Friend>) : ProfileState()
    object Error : ProfileState()
}