package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.model.SearchEvent
import com.example.simbirsoft_android_practice.utils.generateRandomString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

class OrganizationListViewModel @Inject constructor() : ViewModel() {

    private val _organizations = MutableStateFlow<List<SearchEvent>>(emptyList())
    val organizations: StateFlow<List<SearchEvent>> = _organizations.asStateFlow()

    init {
        refreshOrganizationList()
    }

    fun refreshOrganizationList() {
        _organizations.value = generateOrganizationList()
    }

    private fun generateOrganizationList(): List<SearchEvent> {
        return List(5) {
            SearchEvent(
                Random.nextInt(1, 100),
                generateRandomString(),
            )
        }
    }
}
