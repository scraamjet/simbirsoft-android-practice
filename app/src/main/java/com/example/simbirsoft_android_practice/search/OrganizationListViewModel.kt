package com.example.simbirsoft_android_practice.search

import androidx.lifecycle.ViewModel
import com.example.simbirsoft_android_practice.model.SearchEvent
import com.example.simbirsoft_android_practice.utils.generateRandomString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.random.Random

private const val ORGANIZATIONS_LIST_SIZE = 5
private const val EVENT_ID_MIN = 1
private const val EVENT_ID_MAX = 100

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
            return List(ORGANIZATIONS_LIST_SIZE) {
                SearchEvent(
                    Random.nextInt(EVENT_ID_MIN, EVENT_ID_MAX),
                    generateRandomString(),
                )
            }
        }
    }
