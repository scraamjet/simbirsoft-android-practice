package com.example.simbirsoft_android_practice.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.simbirsoft_android_practice.data.database.entity.EventCategoryCrossRef
import com.example.simbirsoft_android_practice.data.database.entity.EventWithCategories
import com.example.simbirsoft_android_practice.data.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Transaction
    @Query("SELECT * FROM events")
    fun getAllEvents(): Flow<List<EventWithCategories>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventCategoryCrossRefs(crossRefs: List<EventCategoryCrossRef>)
}
