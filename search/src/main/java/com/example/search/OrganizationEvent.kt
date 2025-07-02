package com.example.search

sealed interface OrganizationEvent {
    data object Load : OrganizationEvent
}
