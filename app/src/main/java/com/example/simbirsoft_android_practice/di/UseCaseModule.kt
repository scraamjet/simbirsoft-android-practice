package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.domain.FilterPreferencesUseCaseImpl
import com.example.search.domain.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.data.usecase.ProcessNewsUseCaseImpl
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.StartEventServiceUseCase
import com.example.search.domain.EventServiceUseCase
import com.example.simbirsoft_android_practice.data.usecase.EventServiceUseCaseImpl
import com.example.simbirsoft_android_practice.domain.StartEventServiceUseCaseImpl
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
    fun bindProcessNewsUseCase(newsProcessorImpl: ProcessNewsUseCaseImpl): ProcessNewsUseCase

    @Binds
    @Singleton
    fun bindStartEventServiceUseCase(impl: StartEventServiceUseCaseImpl): StartEventServiceUseCase
}
