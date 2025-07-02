package com.example.search

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): SearchComponent
    }

    fun injectSearchContainerFragment(fragment: SearchContainerFragment)
    fun injectEventListFragment(fragment: EventListFragment)
    fun injectOrganizationListFragment(fragment: OrganizationListFragment)

    fun viewModelFactory(): ViewModelProvider.Factory
}
