package com.example.simbirsoft_android_practice

import android.app.Application
import androidx.room.Room
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.database.AppDatabase

private const val DATABASE_NAME = "app_database"

class App : Application(), RepositoryProvider {
    private lateinit var database: AppDatabase

    override val eventRepository: EventRepository by lazy {
        EventRepository(JsonAssetExtractor(this), database.eventDao())
    }

    override val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(JsonAssetExtractor(this), database.categoryDao())
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}

