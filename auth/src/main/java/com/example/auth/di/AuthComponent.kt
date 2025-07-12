package com.example.auth.di

import android.content.Context
import com.example.auth.presentation.AuthorizationFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AuthComponent
    }

    fun injectAuthFragment(fragment: AuthorizationFragment)
}
