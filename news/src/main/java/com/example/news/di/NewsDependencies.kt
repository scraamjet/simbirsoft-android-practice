package com.example.news.di

import androidx.lifecycle.ViewModelProvider

interface NewsDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
}
