package com.example.help.di

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import com.example.help.domain.CategoriesHelpUseCase
import com.example.help.domain.CategoriesHelpUseCaseImpl
import com.example.help.presentation.HelpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface HelpModule {

    @Binds
    @IntoMap
    @ViewModelKey(HelpViewModel::class)
    fun bindHelpViewModel(viewModel: HelpViewModel): ViewModel

    @Binds
    fun bindCategoriesHelpUseCase(impl: CategoriesHelpUseCaseImpl): CategoriesHelpUseCase
}
