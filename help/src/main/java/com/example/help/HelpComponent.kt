package com.example.help

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [HelpModule::class])
interface HelpComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): HelpComponent
    }

    fun injectHelpFragment(fragment: HelpFragment)

    fun viewModelFactory(): ViewModelProvider.Factory
}
