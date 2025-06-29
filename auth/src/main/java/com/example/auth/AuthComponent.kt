package com.example.auth

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [AuthModule::class])
interface AuthComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AuthComponent
    }

    fun injectAuthFragment(fragment: AuthorizationFragment)

    fun viewModelFactory(): ViewModelProvider.Factory
}
