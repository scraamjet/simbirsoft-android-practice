package com.example.simbirsoft_android_practice.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.simbirsoft_android_practice.data.database.converter.ListConverter
import com.example.simbirsoft_android_practice.data.database.dao.CategoryDao
import com.example.simbirsoft_android_practice.data.database.dao.EventDao
import com.example.simbirsoft_android_practice.data.database.entity.CategoryEntity
import com.example.simbirsoft_android_practice.data.database.entity.EventCategoryCrossRef
import com.example.simbirsoft_android_practice.data.database.entity.EventEntity

@Database(
    entities = [
        EventEntity::class,
        CategoryEntity::class,
        EventCategoryCrossRef::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    abstract fun categoryDao(): CategoryDao
}
