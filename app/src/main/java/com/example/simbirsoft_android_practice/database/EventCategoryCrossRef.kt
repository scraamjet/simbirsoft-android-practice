package com.example.simbirsoft_android_practice.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "event_category_cross_ref",
    primaryKeys = ["event_id", "category_id"],
    foreignKeys = [
        ForeignKey(entity = EventEntity::class, parentColumns = ["id"], childColumns = ["event_id"]),
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["id"], childColumns = ["category_id"]),
    ],
)
data class EventCategoryCrossRef(
    @ColumnInfo(name = "event_id")
    val eventId: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
)
