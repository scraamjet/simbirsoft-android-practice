package com.example.simbirsoft_android_practice.data.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class EventWithCategories(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy =
            Junction(
                value = EventCategoryCrossRef::class,
                parentColumn = "event_id",
                entityColumn = "category_id",
            ),
    )
    val categories: List<CategoryEntity>,
)
