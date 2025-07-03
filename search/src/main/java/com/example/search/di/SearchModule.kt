package com.example.search.di

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import com.example.search.domain.EventUseCaseImpl
import com.example.search.domain.EventsUseCase
import com.example.search.presentation.events.EventListViewModel
import com.example.search.presentation.organizations.OrganizationListUseCase
import com.example.search.presentation.organizations.OrganizationListUseCaseImpl
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
    fun bindEventsUseCase(impl: EventUseCaseImpl): EventsUseCase

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
