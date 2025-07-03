package com.example.search.presentation.organizations

import com.example.core.model.SearchEvent

sealed class OrganizationListState {
    data object Idle : OrganizationListState()
    data class Success(val organizations: List<SearchEvent>) : OrganizationListState()
}
