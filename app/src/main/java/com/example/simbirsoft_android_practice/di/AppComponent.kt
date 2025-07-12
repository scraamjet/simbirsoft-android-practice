package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.profile.di.ProfileComponent
import com.example.profile.di.ProfileDependencies
import com.example.simbirsoft_android_practice.App
import com.example.simbirsoft_android_practice.presentation.auth.AuthorizationFragment
import com.example.simbirsoft_android_practice.presentation.filter.FilterFragment
import com.example.simbirsoft_android_practice.presentation.help.HelpFragment
import com.example.simbirsoft_android_practice.presentation.main.MainActivity
import com.example.simbirsoft_android_practice.presentation.news.NewsDetailFragment
import com.example.simbirsoft_android_practice.presentation.news.NewsFragment
import com.example.simbirsoft_android_practice.presentation.search.EventListFragment
import com.example.simbirsoft_android_practice.presentation.search.OrganizationListFragment
import com.example.simbirsoft_android_practice.presentation.search.SearchContainerFragment
import com.example.simbirsoft_android_practice.presentation.service.EventService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        AssetModule::class,
        NetworkModule::class,
        GsonModule::class,
        DatabaseModule::class,
        DataStoreModule::class,
        ViewModelModule::class,
        UseCaseModule::class,
        NavigationModule::class,
        InteractorModule::class,
        NavigationModule::class,
        AppModule::class,
    ],
)
interface AppComponent : ProfileDependencies {
    fun profileComponentFactory(): ProfileComponent.Factory
    fun inject(app: App)
    fun inject(service: EventService)
    fun inject(activity: MainActivity)
    fun inject(fragment: HelpFragment)
    fun inject(fragment: NewsFragment)
    fun inject(fragment: NewsDetailFragment)
    fun inject(fragment: FilterFragment)
    fun inject(fragment: EventListFragment)
    fun inject(fragment: OrganizationListFragment)
    fun inject(fragment: SearchContainerFragment)
    fun inject(fragment: AuthorizationFragment)
}

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent
