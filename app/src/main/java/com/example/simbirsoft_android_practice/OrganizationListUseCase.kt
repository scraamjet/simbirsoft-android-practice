package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.SearchEvent

interface OrganizationListUseCase {
    fun invoke(): List<SearchEvent>
}
