package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.domain.model.Friend

interface ProfileUseCase {
    fun loadFriends(): List<Friend>
}