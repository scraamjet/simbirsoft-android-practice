package com.example.search.di

import android.content.Context
import com.example.search.presentation.events.EventListFragment
import com.example.search.presentation.organizations.OrganizationListFragment
import com.example.search.presentation.search.SearchContainerFragment
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
}
