package com.example.simbirsoft_android_practice.presentation.search

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

    private val _uiState = MutableStateFlow<OrganizationListState>(OrganizationListState.Idle)
    val uiState: StateFlow<OrganizationListState> = _uiState.asStateFlow()

    fun onEvent(event: OrganizationEvent) {
        when (event) {
            is OrganizationEvent.Load -> loadOrganizations()
        }
    }

    private fun loadOrganizations() {
        val organizations: List<SearchEvent> = organizationListUseCase.invoke()
        _uiState.value = OrganizationListState.Success(organizations)
    }
}

