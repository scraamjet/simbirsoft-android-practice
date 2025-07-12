package com.example.filter.di

import androidx.lifecycle.ViewModelProvider

interface FilterDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
}
