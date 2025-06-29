package com.example.profile.di

import androidx.lifecycle.ViewModelProvider
import com.example.core.navigation.AppRouter

interface ProfileDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
    fun appRouter(): AppRouter
}


