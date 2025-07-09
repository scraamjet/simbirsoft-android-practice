package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.navigation.AppRouter
import com.example.simbirsoft_android_practice.navigation.AppRouterImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface NavigationModule {

    @Binds
    @Singleton
    fun bindAppRouter(impl: AppRouterImpl): AppRouter
}