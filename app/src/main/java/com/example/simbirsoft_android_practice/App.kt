package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.simbirsoft_android_practice.di.AppComponent
import com.example.simbirsoft_android_practice.di.AppModule
import com.example.simbirsoft_android_practice.di.DaggerAppComponent

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent =
            DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .build()

        appComponent.inject(this)
    }
}
