package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.core.EventRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {
    @Binds
    fun bindNewsUseCase(impl: NewsUseCaseImpl): NewsUseCase

    @Binds
    fun bindNewsDetailUseCase(impl: NewsDetailUseCaseImpl): NewsDetailUseCase

    @Binds
    fun bindCategoriesHelpUseCase(impl: GetHelpCategoriesUseCaseImpl): CategoriesHelpUseCase

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
}
