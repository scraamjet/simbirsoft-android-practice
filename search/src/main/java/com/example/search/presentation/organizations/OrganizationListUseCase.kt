package com.example.search.presentation.organizations

import com.example.core.model.SearchEvent

interface OrganizationListUseCase {
    fun invoke(): List<SearchEvent>
}
