package com.example.search.presentation.organizations

sealed interface OrganizationEvent {
    data object Load : OrganizationEvent
}
