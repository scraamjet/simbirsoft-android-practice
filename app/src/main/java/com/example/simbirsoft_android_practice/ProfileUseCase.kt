package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.Friend

interface ProfileUseCase {
    fun loadFriends(): List<Friend>
}