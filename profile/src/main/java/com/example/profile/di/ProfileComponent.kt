package com.example.profile.di

import android.content.Context
import com.example.profile.presentation.profile.ProfileFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [ProfileModule::class])
interface ProfileComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ProfileComponent
    }

    fun injectProfileFragment(fragment: ProfileFragment)
}


