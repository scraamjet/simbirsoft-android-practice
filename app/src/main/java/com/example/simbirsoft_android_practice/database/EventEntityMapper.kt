package com.example.simbirsoft_android_practice.database

import com.example.simbirsoft_android_practice.model.Event

object EventEntityMapper {
    private fun toEvent(eventWithCategories: EventWithCategories): Event {
        return Event(
            id = eventWithCategories.event.id,
            name = eventWithCategories.event.name,
            startDate = eventWithCategories.event.startDate,
            endDate = eventWithCategories.event.endDate,
            description = eventWithCategories.event.description,
            status = eventWithCategories.event.status,
            photos = eventWithCategories.event.photos,
            createAt = eventWithCategories.event.createAt,
            phone = eventWithCategories.event.phone,
            address = eventWithCategories.event.address,
            organisation = eventWithCategories.event.organisation,
            categoryIds = eventWithCategories.categories.map { category -> category.id },
        )
    }

    fun fromEventList(events: List<Event>): Pair<List<EventEntity>, List<EventCategoryCrossRef>> {
        val eventEntities =
            events.map { event ->
                EventEntity(
                    id = event.id,
                    name = event.name,
                    startDate = event.startDate,
                    endDate = event.endDate,
                    description = event.description,
                    status = event.status,
                    photos = event.photos,
                    createAt = event.createAt,
                    phone = event.phone,
                    address = event.address,
                    organisation = event.organisation,
                )
            }

        val crossRefs =
            events.flatMap { event ->
                event.categoryIds.map { categoryId ->
                    EventCategoryCrossRef(eventId = event.id, categoryId = categoryId)
                }
            }

        return eventEntities to crossRefs
    }

    fun toEventList(eventsWithCategories: List<EventWithCategories>): List<Event> {
        return eventsWithCategories.map { eventWithCat -> toEvent(eventWithCat) }
    }
}
