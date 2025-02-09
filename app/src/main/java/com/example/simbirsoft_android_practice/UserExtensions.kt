package com.example.simbirsoft_android_practice

fun User.checkAdult(): Result<Unit> {
    return if (age >= 18) {
        println("User $name is an adult.")
        Result.success(Unit)
    } else {
        Result.failure(IllegalArgumentException("User $name is not an adult."))
    }
}