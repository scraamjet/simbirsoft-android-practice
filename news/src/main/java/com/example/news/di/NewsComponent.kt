package com.example.news.di

import android.content.Context
import com.example.news.presentation.news.NewsFragment
import com.example.news.presentation.newsdetail.NewsDetailFragment
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
}
