package com.example.auth

import androidx.lifecycle.ViewModelProvider
import com.example.core.navigation.AppRouter

interface AuthDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
    fun appRouter(): AppRouter
}
