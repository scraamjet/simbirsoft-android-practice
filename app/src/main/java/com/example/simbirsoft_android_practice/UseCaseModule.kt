package com.example.simbirsoft_android_practice

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UseCaseModule {

    @Binds
    @Singleton
    fun bindNewsServiceUseCase(impl: NewsServiceUseCaseImpl): NewsServiceUseCase
}
