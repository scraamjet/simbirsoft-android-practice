package com.example.simbirsoft_android_practice.core.utils

import java.util.UUID

private const val RANDOM_STRING_LENGTH = 20

fun generateRandomString(): String {
    return UUID.randomUUID()
        .toString()
        .replace("-", "")
        .take(RANDOM_STRING_LENGTH)
}
