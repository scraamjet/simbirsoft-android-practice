package com.example.auth.di

import androidx.lifecycle.ViewModel
import com.example.auth.presentation.AuthorizationViewModel
import com.example.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface AuthModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthorizationViewModel::class)
    fun bindAuthorizationViewModel(viewModel: AuthorizationViewModel): ViewModel
}
