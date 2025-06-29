package com.example.profile

import androidx.lifecycle.ViewModelProvider
import com.example.core.navigation.AppRouter

interface ProfileDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
    fun appRouter(): AppRouter
}


