package com.example.simbirsoft_android_practice.di

import com.example.core.navigation.AppRouter
import com.example.simbirsoft_android_practice.navigation.AppRouterImpl
import dagger.Binds
import dagger.Module

@Module
interface NavigationModule {

    @Binds
    fun bindAppRouter(impl: AppRouterImpl): AppRouter
}
