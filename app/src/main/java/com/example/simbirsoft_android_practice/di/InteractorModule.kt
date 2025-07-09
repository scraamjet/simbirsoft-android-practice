package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.NewsBadgeCountInteractor
import com.example.simbirsoft_android_practice.NewsBadgeCountInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface InteractorModule {
    @Binds
    @Singleton
    fun bindNewsBadgeInteractor(impl: NewsBadgeCountInteractorImpl): NewsBadgeCountInteractor
}
