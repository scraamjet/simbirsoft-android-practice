package com.example.simbirsoft_android_practice

inline fun auth(updateCache: () -> Unit) {
    println("Authenticating user...")
    updateCache()
    println("Authentication process completed.")
}

inline fun auth(
    user: User,
    authCallback: AuthCallback,
    updateCache: () -> Unit
) {
    println("Authenticating user ${user.name}...")
    user.checkAdult()
        .fold(
            onSuccess = {
                authCallback.authSuccess()
                updateCache()
            },
            onFailure = {
                authCallback.authFailed()
            }
        )
    println("Authentication process completed.")
}