package com.example.search

import com.example.core.model.SearchEvent

interface OrganizationListUseCase {
    fun invoke(): List<SearchEvent>
}
