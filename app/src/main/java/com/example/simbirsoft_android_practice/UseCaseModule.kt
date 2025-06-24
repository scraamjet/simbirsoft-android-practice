package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.domain.usecase.CategoriesFilterUseCase
import com.example.simbirsoft_android_practice.domain.usecase.CategoriesHelpUseCase
import com.example.simbirsoft_android_practice.domain.usecase.EventsUseCase
import com.example.simbirsoft_android_practice.domain.usecase.FilterPreferencesUseCase
import com.example.simbirsoft_android_practice.domain.usecase.NewsDetailUseCase
import com.example.simbirsoft_android_practice.domain.usecase.NewsPreferencesUseCase
import com.example.simbirsoft_android_practice.domain.usecase.NewsUseCase
import com.example.simbirsoft_android_practice.domain.usecase.OrganizationListUseCase
import com.example.simbirsoft_android_practice.domain.usecase.ProfileUseCase
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {
    @Binds
    fun bindNewsUseCase(impl: NewsUseCaseImpl): NewsUseCase

    @Binds
    fun bindNewsDetailUseCase(impl: NewsDetailUseCaseImpl): NewsDetailUseCase

    @Binds
    fun bindCategoriesHelpUseCase(impl: CategoriesHelpUseCaseImpl): CategoriesHelpUseCase

    @Binds
    fun bindCategoriesFilterUseCase(impl: CategoriesFilterUseCaseImpl): CategoriesFilterUseCase

    @Binds
    fun bindFilterPreferencesUseCase(impl: FilterPreferencesUseCaseImpl): FilterPreferencesUseCase

    @Binds
    fun bindNewsPreferencesUseCase(impl: NewsPreferencesUseCaseImpl): NewsPreferencesUseCase

    @Binds
    fun bindOrganizationListUseCase(impl: OrganizationListUseCaseImpl): OrganizationListUseCase

    @Binds
    fun bindEventListUseCase(impl: EventUseCaseImpl): EventsUseCase

    @Binds
    fun bindProfileUseCase(impl:ProfileUseCaseImpl): ProfileUseCase
}
