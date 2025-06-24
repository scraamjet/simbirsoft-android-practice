package com.example.simbirsoft_android_practice.presentation.search

import com.example.simbirsoft_android_practice.domain.model.SearchEvent

sealed interface OrganizationUiState {
    data object Loading : OrganizationUiState
    data class Success(val organizations: List<SearchEvent>) : OrganizationUiState
}
