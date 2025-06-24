package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.domain.usecase.OrganizationListUseCase
import com.example.simbirsoft_android_practice.domain.model.SearchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class OrganizationListViewModel @Inject constructor(
    private val organizationListUseCase: OrganizationListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<OrganizationUiState>(OrganizationUiState.Loading)
    val uiState: StateFlow<OrganizationUiState> = _uiState.asStateFlow()

    init {
        onEvent(OrganizationUiEvent.LoadOrganizations)
    }

    fun onEvent(event: OrganizationUiEvent) {
        when (event) {
            is OrganizationUiEvent.LoadOrganizations -> loadOrganizations()
        }
    }

    private fun loadOrganizations() {
        val organizations: List<SearchEvent> = organizationListUseCase.invoke()
        _uiState.value = OrganizationUiState.Success(organizations)
    }
}
