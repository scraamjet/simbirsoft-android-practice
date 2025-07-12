package com.example.simbirsoft_android_practice.di

import android.content.Context
import com.example.simbirsoft_android_practice.utils.JsonAssetExtractor
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AssetModule {
    @Provides
    @Singleton
    fun provideJsonAssetExtractor(context: Context): JsonAssetExtractor {
        return JsonAssetExtractor(context)
    }
}
