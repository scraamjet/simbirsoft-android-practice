package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.domain.usecase.FilterPreferencesUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCaseImpl
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.StartEventServiceUseCase
import com.example.simbirsoft_android_practice.domain.usecase.EventServiceUseCase
import com.example.simbirsoft_android_practice.domain.usecase.EventServiceUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.StartEventServiceUseCaseImpl
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
    fun bindEventServiceUseCase(impl: EventServiceUseCaseImpl): EventServiceUseCase

    @Binds
    @Singleton
    fun bindProcessNewsUseCase(impl: ProcessNewsUseCaseImpl): ProcessNewsUseCase

    @Binds
    @Singleton
    fun bindStartEventServiceUseCase(impl: StartEventServiceUseCaseImpl): StartEventServiceUseCase
}
