package com.example.search.di

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import com.example.search.domain.EventListUseCaseImpl
import com.example.search.domain.EventListUseCase
import com.example.search.presentation.events.EventListViewModel
import com.example.search.domain.OrganizationListUseCase
import com.example.search.domain.OrganizationListUseCaseImpl
import com.example.search.presentation.organizations.OrganizationListViewModel
import com.example.search.presentation.search.SearchContainerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchModule {

    @Binds
    fun bindOrganizationListUseCase(impl: OrganizationListUseCaseImpl): OrganizationListUseCase

    @Binds
    fun bindEventListUseCase(impl: EventListUseCaseImpl): EventListUseCase

    @Binds
    @IntoMap
    @ViewModelKey(SearchContainerViewModel::class)
    fun bindSearchContainerViewModel(viewModel: SearchContainerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    fun bindEventListViewModel(viewModel: EventListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrganizationListViewModel::class)
    fun bindOrganizationListViewModel(viewModel: OrganizationListViewModel): ViewModel
}
