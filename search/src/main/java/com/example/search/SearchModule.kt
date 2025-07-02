package com.example.search

import androidx.lifecycle.ViewModel
import com.example.core.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface SearchModule {

    @Binds
    fun bindOrganizationListUseCase(impl: OrganizationListUseCaseImpl):OrganizationListUseCase

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
