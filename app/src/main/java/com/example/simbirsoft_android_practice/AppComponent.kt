package com.example.simbirsoft_android_practice

import android.content.Context
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.help.HelpFragment
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.news.NewsDetailFragment
import com.example.simbirsoft_android_practice.news.NewsFragment
import com.example.simbirsoft_android_practice.news.NewsService
import com.example.simbirsoft_android_practice.search.EventListFragment
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
        PreferencesModule::class,
        ViewModelModule::class,
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
}

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent
