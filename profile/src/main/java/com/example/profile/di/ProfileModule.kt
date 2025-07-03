package com.example.profile.di

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import com.example.profile.domain.usecase.ProfileUseCase
import com.example.profile.domain.usecase.ProfileUseCaseImpl
import com.example.profile.presentation.profile.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ProfileModule {

    @Binds
    fun bindProfileUseCase(impl: ProfileUseCaseImpl): ProfileUseCase

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel
}

