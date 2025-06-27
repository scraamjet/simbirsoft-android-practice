package com.example.profile

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ProfileComponent
    }

    fun inject(fragment: ProfileFragment)
}

