package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.simbirsoft_android_practice.App
import com.example.simbirsoft_android_practice.auth.AuthorizationFragment
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.help.HelpFragment
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.news.NewsDetailFragment
import com.example.simbirsoft_android_practice.news.NewsFragment
import com.example.simbirsoft_android_practice.news.NewsService
import com.example.simbirsoft_android_practice.presentation.profile.ProfileFragment
import com.example.simbirsoft_android_practice.search.EventListFragment
import com.example.simbirsoft_android_practice.search.OrganizationListFragment
import com.example.simbirsoft_android_practice.search.SearchContainerFragment
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
        AppModule::class,
    ],
)
interface AppComponent {

    fun inject(app: App)
    fun inject(service: NewsService)
    fun inject(activity: MainActivity)
    fun inject(fragment: HelpFragment)
    fun inject(fragment: NewsFragment)
    fun inject(fragment: NewsDetailFragment)
    fun inject(fragment: FilterFragment)
    fun inject(fragment: EventListFragment)
    fun inject(fragment: OrganizationListFragment)
    fun inject(fragment: SearchContainerFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: AuthorizationFragment)
}

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent
