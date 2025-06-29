package com.example.profile

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ProfileComponent
    }

    fun injectProfileFragment(fragment: ProfileFragment)

    fun viewModelFactory(): ViewModelProvider.Factory
}


