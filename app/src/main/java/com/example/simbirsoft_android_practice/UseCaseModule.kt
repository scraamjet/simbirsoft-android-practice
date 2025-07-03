package com.example.simbirsoft_android_practice

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideNewsServiceUseCase(): NewsServiceUseCase = NewsServiceUseCase()
}