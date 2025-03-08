package com.example.simbirsoft_android_practice.utils

import java.util.UUID

private const val RANDOM_STRING_LENGTH = 20

fun generateRandomString(): String {
    val randomUUID = UUID.randomUUID()
    val uuidString = randomUUID.toString()
    val cleanString = uuidString.replace("-", "")

    return cleanString.take(RANDOM_STRING_LENGTH)
}
