package com.example.simbirsoft_android_practice

inline fun auth(updateCache: () -> Unit) {
    println("Authenticating user...")
    updateCache()
    println("Authentication process completed.")
}