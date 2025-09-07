package com.example.profile.domain.usecase

import com.example.profile.presentation.model.Friend

interface ProfileUseCase {
    fun loadFriends(): List<Friend>
}
