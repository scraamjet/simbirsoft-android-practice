package com.example.background.di

import android.content.Context
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface WorkerComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): WorkerComponent
    }
}
