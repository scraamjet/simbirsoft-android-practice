package com.example.simbirsoft_android_practice.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.di.MultiViewModelFactory
import com.example.core.di.ViewModelKey
import com.example.simbirsoft_android_practice.presentation.filter.FilterViewModel
import com.example.simbirsoft_android_practice.presentation.main.MainViewModel
import com.example.simbirsoft_android_practice.presentation.news.NewsDetailViewModel
import com.example.simbirsoft_android_practice.presentation.news.NewsViewModel
import com.example.simbirsoft_android_practice.presentation.search.EventListViewModel
import com.example.simbirsoft_android_practice.presentation.search.OrganizationListViewModel
import com.example.simbirsoft_android_practice.presentation.search.SearchContainerViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: MultiViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    fun bindNewsViewModel(viewModel: NewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    fun bindFilterViewModel(viewModel: FilterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsDetailViewModel::class)
    fun bindNewsDetailViewModel(viewModel: NewsDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchContainerViewModel::class)
    fun bindSearchContainerViewModel(viewModel: SearchContainerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrganizationListViewModel::class)
    fun bindOrganizationListViewModel(viewModel: OrganizationListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    fun bindEventListViewModel(viewModel: EventListViewModel): ViewModel
}
