package com.example.simbirsoft_android_practice.presentation.search

sealed interface OrganizationUiEvent {
    data object LoadOrganizations : OrganizationUiEvent
}
