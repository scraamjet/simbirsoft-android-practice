package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.data.usecase.EventUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.FilterPreferencesUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.NewsPreferencesUseCaseImpl
import com.example.simbirsoft_android_practice.data.usecase.OrganizationListUseCaseImpl
import com.example.simbirsoft_android_practice.domain.usecase.EventsUseCase
import com.example.core.usecase.FilterPreferencesUseCase
import com.example.core.usecase.NewsBadgeCountUseCase
import com.example.core.usecase.NewsPreferencesUseCase
import com.example.core.usecase.StartNewsServiceUseCase
import com.example.simbirsoft_android_practice.StartNewsServiceUseCaseImpl
import com.example.simbirsoft_android_practice.NewsBadgeCountUseCaseImpl
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
    fun bindNewsPreferencesUseCase(impl: NewsPreferencesUseCaseImpl): NewsPreferencesUseCase

    @Binds
    fun bindOrganizationListUseCase(impl: OrganizationListUseCaseImpl): OrganizationListUseCase

    @Binds
    fun bindEventListUseCase(impl: EventUseCaseImpl): EventsUseCase

    @Binds
    @Singleton
    fun bindStartNewsServiceUseCase(impl: StartNewsServiceUseCaseImpl): StartNewsServiceUseCase

    @Binds
    @Singleton
    fun bindNewsBadgeCountUseCase(impl: NewsBadgeCountUseCaseImpl): NewsBadgeCountUseCase

}
