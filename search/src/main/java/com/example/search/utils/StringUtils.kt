package com.example.search.utils

import java.util.UUID

private const val RANDOM_STRING_LENGTH = 20

fun generateRandomString(): String {
    return UUID.randomUUID()
        .toString()
        .replace("-", "")
        .take(RANDOM_STRING_LENGTH)
}
