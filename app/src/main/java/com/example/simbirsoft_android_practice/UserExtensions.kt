package com.example.simbirsoft_android_practice

fun User.isAdult(): Boolean {
    return if (age >= 18) {
        println("User $name is an adult.")
        true
    } else {
        println("User $name is not an adult.")
        throw UnderageUserException("User $name is not an adult.")
    }
}