package com.example.news

import androidx.lifecycle.ViewModelProvider

interface NewsDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
}
