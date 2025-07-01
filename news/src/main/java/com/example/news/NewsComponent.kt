package com.example.news

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [NewsModule::class])
interface NewsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): NewsComponent
    }

    fun injectNewsFragment(fragment: NewsFragment)
    fun injectNewsDetailFragment(fragment: NewsDetailFragment)

    fun viewModelFactory(): ViewModelProvider.Factory
}
