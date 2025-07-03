package com.example.search.di

import androidx.lifecycle.ViewModelProvider

interface SearchDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
}
