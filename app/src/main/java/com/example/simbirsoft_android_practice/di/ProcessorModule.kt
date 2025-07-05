package com.example.simbirsoft_android_practice.di

import com.example.simbirsoft_android_practice.NewsProcessor
import com.example.simbirsoft_android_practice.NewsProcessorImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface ProcessorModule {
    @Binds
    @Singleton
    fun bindNewsProcessor(newsProcessorImpl: NewsProcessorImpl): NewsProcessor
}