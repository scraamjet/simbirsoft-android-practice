package com.example.help

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
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
