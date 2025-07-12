package com.example.simbirsoft_android_practice.presentation.search

sealed interface OrganizationEvent {
    data object Load : OrganizationEvent
}
