package com.example.simbirsoft_android_practice.domain.usecase

import com.example.simbirsoft_android_practice.model.SearchEvent

interface OrganizationListUseCase {
    fun invoke(): List<SearchEvent>
}
