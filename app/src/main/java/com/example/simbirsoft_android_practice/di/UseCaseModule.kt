package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.domain.usecase.EventServiceUseCase
import com.example.simbirsoft_android_practice.data.usecase.EventServiceUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.ProcessNewsUseCase
import com.example.simbirsoft_android_practice.data.usecase.ProcessNewsUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.FilterPreferencesUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.NewsDetailUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.NewsPreferencesUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.NewsUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.OrganizationListUseCaseImpl
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.domain.usecase.NewsDetailUseCase
import com.example.core.usecase.NewsPreferencesUseCase
import com.example.core.usecase.StartEventServiceUseCase
import com.example.filter.usecase.CategoriesFilterUseCase
import com.example.filter.usecase.CategoriesFilterUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.NewsUseCase
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
    fun bindNewsUseCase(impl: NewsUseCaseImpl): NewsUseCase

    @Binds
    @Singleton
    fun bindNewsDetailUseCase(impl: NewsDetailUseCaseImpl): NewsDetailUseCase

    @Binds
    @Singleton
    fun bindFilterPreferencesUseCase(impl: FilterPreferencesUseCaseImpl): FilterPreferencesUseCase

    @Binds
    @Singleton
    fun bindNewsPreferencesUseCase(impl: NewsPreferencesUseCaseImpl): NewsPreferencesUseCase

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
