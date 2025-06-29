package com.example.filter.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.filter.presentation.FilterFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [FilterModule::class])
interface FilterComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): FilterComponent
    }

    fun injectFilterFragment(fragment: FilterFragment)

    fun viewModelFactory(): ViewModelProvider.Factory
}
