package com.example.simbirsoft_android_practice

import android.content.Context
import androidx.room.Room
import com.example.simbirsoft_android_practice.database.AppDatabase
import com.example.simbirsoft_android_practice.database.CategoryDao
import com.example.simbirsoft_android_practice.database.EventDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

private const val DATABASE_NAME = "app_database"

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
            .build()

    @Provides
    fun provideEventDao(db: AppDatabase): EventDao = db.eventDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()
}
