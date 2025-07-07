package com.example.help.di

import android.content.Context
import com.example.help.presentation.HelpFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [HelpModule::class])
interface HelpComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): HelpComponent
    }

    fun injectHelpFragment(fragment: HelpFragment)
}
