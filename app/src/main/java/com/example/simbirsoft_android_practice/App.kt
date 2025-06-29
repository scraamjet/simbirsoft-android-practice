package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.profile.ProfileComponent
import com.example.profile.ProfileComponentProvider
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

    override fun getProfileComponent(): ProfileComponent {
        return appComponent.profileComponentFactory().create(this)
    }
}

