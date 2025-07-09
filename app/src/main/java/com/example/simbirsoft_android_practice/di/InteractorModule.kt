package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.domain.interactor.NewsBadgeCountInteractor
import com.example.simbirsoft_android_practice.data.interactor.NewsBadgeCountInteractorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface InteractorModule {
    @Binds
    @Singleton
    fun bindNewsBadgeInteractor(impl: NewsBadgeCountInteractorImpl): NewsBadgeCountInteractor
}
