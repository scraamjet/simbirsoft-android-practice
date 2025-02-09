package com.example.simbirsoft_android_practice

import java.time.Instant

data class User(
    val id: Long,
    val name: String,
    val age: Int,
    val type: Type
) {
    val startTime: Instant by lazy { Instant.now() }
}