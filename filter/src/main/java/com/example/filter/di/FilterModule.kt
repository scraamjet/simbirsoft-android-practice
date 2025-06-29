package com.example.filter.di

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import com.example.filter.usecase.CategoriesFilterUseCase
import com.example.filter.usecase.CategoriesFilterUseCaseImpl
import com.example.filter.presentation.FilterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface FilterModule {

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    fun bindFilterViewModel(viewModel: FilterViewModel): ViewModel

    @Binds
    fun bindCategoriesFilterUseCase(impl: CategoriesFilterUseCaseImpl): CategoriesFilterUseCase
}
