package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.domain.usecase.EventServiceUseCase
import com.example.simbirsoft_android_practice.data.usecase.EventServiceUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.data.usecase.ProcessNewsUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.FilterPreferencesUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.OrganizationListUseCaseImpl
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.StartEventServiceUseCase
import com.example.simbirsoft_android_practice.StartEventServiceUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.EventListUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.EventListUseCase
import com.example.simbirsoft_android_practice.domain.usecase.OrganizationListUseCase
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
    fun bindOrganizationListUseCase(impl: OrganizationListUseCaseImpl): OrganizationListUseCase

    @Binds
    @Singleton
    fun bindEventListUseCase(impl: EventListUseCaseImpl): EventListUseCase

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
