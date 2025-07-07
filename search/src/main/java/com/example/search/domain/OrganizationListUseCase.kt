package com.example.search.domain

import com.example.core.model.SearchEvent

interface OrganizationListUseCase {
    fun invoke(): List<SearchEvent>
}
