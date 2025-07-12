package com.example.simbirsoft_android_practice

import android.app.Application
import com.example.auth.di.AuthComponent
import com.example.auth.di.AuthComponentProvider
import com.example.filter.di.FilterComponent
import com.example.filter.di.FilterComponentProvider
import com.example.help.di.HelpComponent
import com.example.help.di.HelpComponentProvider
import com.example.profile.di.ProfileComponent
import com.example.profile.di.ProfileComponentProvider
import com.example.simbirsoft_android_practice.di.AppComponent
import com.example.simbirsoft_android_practice.di.AppModule
import com.example.simbirsoft_android_practice.di.DaggerAppComponent

class App : Application(), ProfileComponentProvider, AuthComponentProvider, HelpComponentProvider,
    FilterComponentProvider {
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

    override fun provideAuthComponent(): AuthComponent {
        return appComponent.authComponentFactory().create(this)
    }

    override fun provideHelpComponent(): HelpComponent {
        return appComponent.helpComponentFactory().create(this)
    }

    override fun provideFilterComponent(): FilterComponent {
        return appComponent.filterComponentFactory().create(this)
    }
}

