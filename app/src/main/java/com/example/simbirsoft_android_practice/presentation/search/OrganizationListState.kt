package com.example.simbirsoft_android_practice.presentation.search

import com.example.simbirsoft_android_practice.domain.model.SearchEvent

sealed class OrganizationListState {
    data object Idle : OrganizationListState()
    data class Success(val organizations: List<SearchEvent>) : OrganizationListState()
}
