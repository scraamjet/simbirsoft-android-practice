package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.profile.di.ProfileComponent
import com.example.profile.di.ProfileComponentProvider
import com.example.simbirsoft_android_practice.di.AppComponent
import com.example.simbirsoft_android_practice.di.AppModule
import com.example.simbirsoft_android_practice.di.DaggerAppComponent

class App : Application(), ProfileComponentProvider {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        appComponent.inject(this)
    }

    override fun provideProfileComponent(): ProfileComponent {
        return appComponent.profileComponentFactory().create(this)
    }
}

