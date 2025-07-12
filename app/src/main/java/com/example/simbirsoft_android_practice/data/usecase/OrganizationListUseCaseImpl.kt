package com.example.simbirsoft_android_practice.data.usecase

import com.example.simbirsoft_android_practice.core.utils.generateRandomString
import com.example.simbirsoft_android_practice.domain.model.SearchEvent
import com.example.simbirsoft_android_practice.domain.usecase.OrganizationListUseCase
import javax.inject.Inject
import kotlin.random.Random

private const val ORGANIZATIONS_LIST_SIZE = 5
private const val EVENT_ID_MIN = 1
private const val EVENT_ID_MAX = 100

class OrganizationListUseCaseImpl @Inject constructor() : OrganizationListUseCase {
    override fun invoke(): List<SearchEvent> {
        return List(ORGANIZATIONS_LIST_SIZE) {
            SearchEvent(
                id = Random.nextInt(EVENT_ID_MIN, EVENT_ID_MAX),
                title = generateRandomString()
            )
        }
    }
}
