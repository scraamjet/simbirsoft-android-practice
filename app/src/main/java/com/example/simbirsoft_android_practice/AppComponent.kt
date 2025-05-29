package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.news.NewsService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RepositoryModule::class,
        NetworkModule::class,
        GsonModule::class,
        DatabaseModule::class,
        PreferencesModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    fun inject(app: App)
    fun inject(service: NewsService)
    fun inject(activity: MainActivity)
}
