package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.auth.di.AuthComponent
import com.example.auth.di.AuthDependencies
import com.example.profile.di.ProfileComponent
import com.example.profile.di.ProfileDependencies
import com.example.simbirsoft_android_practice.App
import com.example.auth.presentation.AuthorizationFragment
import com.example.filter.di.FilterComponent
import com.example.filter.di.FilterDependencies
import com.example.help.di.HelpComponent
import com.example.help.di.HelpDependencies
import com.example.filter.presentation.FilterFragment
import com.example.help.presentation.HelpFragment
import com.example.news.di.NewsComponent
import com.example.news.di.NewsDependencies
import com.example.simbirsoft_android_practice.presentation.main.MainActivity
import com.example.news.presentation.newsdetail.NewsDetailFragment
import com.example.news.presentation.news.NewsFragment
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
interface AppComponent : ProfileDependencies, AuthDependencies, HelpDependencies,
    FilterDependencies, NewsDependencies {
    fun authComponentFactory(): AuthComponent.Factory
    fun profileComponentFactory(): ProfileComponent.Factory
    fun helpComponentFactory(): HelpComponent.Factory
    fun filterComponentFactory(): FilterComponent.Factory
    fun newsComponentFactory(): NewsComponent.Factory
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
