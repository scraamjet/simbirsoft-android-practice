package com.example.simbirsoft_android_practice

import com.example.simbirsoft_android_practice.model.SearchEvent
import com.example.simbirsoft_android_practice.utils.generateRandomString
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
