package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.OrganizationListUseCase
import com.example.simbirsoft_android_practice.model.SearchEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class OrganizationListViewModel @Inject constructor(
    private val organizationListUseCase: OrganizationListUseCase
) : ViewModel() {

    private val _organizations = MutableStateFlow<List<SearchEvent>>(emptyList())
    val organizations: StateFlow<List<SearchEvent>> = _organizations.asStateFlow()

    init {
        refreshOrganizationList()
    }

    fun refreshOrganizationList() {
        _organizations.value = organizationListUseCase.invoke()
    }
}
