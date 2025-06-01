package com.example.simbirsoft_android_practice

import android.app.Application


class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(applicationContext))
            .build()

        appComponent.inject(this)
    }
}


