package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.auth.di.AuthComponent
import com.example.auth.di.AuthComponentProvider
import com.example.profile.di.ProfileComponent
import com.example.profile.di.ProfileComponentProvider
import com.example.simbirsoft_android_practice.di.AppComponent
import com.example.simbirsoft_android_practice.di.AppModule
import com.example.simbirsoft_android_practice.di.DaggerAppComponent

class App : Application(), ProfileComponentProvider, AuthComponentProvider {
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

    override fun provideAuthComponent(): AuthComponent {
        return appComponent.authComponentFactory().create(this)
    }
}

