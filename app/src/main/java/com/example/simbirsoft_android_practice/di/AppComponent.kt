package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.auth.di.AuthComponent
import com.example.core.NotificationComponent
import com.example.core.di.MultiViewModelFactory
import com.example.profile.di.ProfileComponent
import com.example.simbirsoft_android_practice.App
import com.example.filter.di.FilterComponent
import com.example.help.di.HelpComponent
import com.example.news.di.NewsComponent
import com.example.worker.WorkerComponent
import com.example.simbirsoft_android_practice.presentation.main.MainActivity
import com.example.search.di.SearchComponent
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
        WorkManagerModule::class,
        NotificationModule::class,
        AppModule::class,
    ],
)
interface AppComponent {
    fun authComponentFactory(): AuthComponent.Factory
    fun profileComponentFactory(): ProfileComponent.Factory
    fun helpComponentFactory(): HelpComponent.Factory
    fun filterComponentFactory(): FilterComponent.Factory
    fun newsComponentFactory(): NewsComponent.Factory
    fun searchComponentFactory(): SearchComponent.Factory

    val multiViewModelFactory: MultiViewModelFactory
    fun notificationComponent(): NotificationComponent
    fun workerComponentFactory(): WorkerComponent.Factory

    fun inject(app: App)
    fun inject(service: EventService)
    fun inject(activity: MainActivity)
}

val Context.appComponent: AppComponent
    get() = (applicationContext as App).appComponent
