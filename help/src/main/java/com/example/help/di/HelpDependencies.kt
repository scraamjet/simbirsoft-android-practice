package com.example.help.di

import androidx.lifecycle.ViewModelProvider

interface HelpDependencies {
    fun viewModelFactory(): ViewModelProvider.Factory
}
