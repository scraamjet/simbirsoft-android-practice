package com.example.simbirsoft_android_practice.search

sealed interface OrganizationUiEvent {
    data object LoadOrganizations : OrganizationUiEvent
}
