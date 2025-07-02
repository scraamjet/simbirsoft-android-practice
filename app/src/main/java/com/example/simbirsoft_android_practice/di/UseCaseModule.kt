package com.example.simbirsoft_android_practice.di

 import com.example.simbirsoft_android_practice.data.usecase.FilterPreferencesUseCaseImpl
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.StartNewsServiceUseCase
import com.example.simbirsoft_android_practice.StartNewsServiceUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UseCaseModule {

    @Binds
    @Singleton
    fun bindFilterPreferencesUseCase(impl: FilterPreferencesUseCaseImpl): FilterPreferencesUseCase

    @Binds
    @Singleton
    fun bindStartNewsServiceUseCase(impl: StartNewsServiceUseCaseImpl): StartNewsServiceUseCase
}
