package com.example.simbirsoft_android_practice

inline fun auth(
    user: User,
    authCallback: AuthCallback,
    updateCache: () -> Unit
) {
    println("Authenticating user ${user.name}...")
    try {
        if (user.isAdult()) {
            authCallback.authSuccess()
            updateCache()
        }
    } catch (e: UnderageUserException) {
        authCallback.authFailed()
        println(e.message)
    }
    println("Authentication process completed.")
}

fun doAction(action: Action, authCallback: AuthCallback) {
    when (action) {
        is Action.Registration -> println("Auth started: Registration")
        is Action.Login -> {
            println("Auth started: Login for user ${action.user.name}")
            auth(action.user, authCallback) {
                println("Updating cache after login...")
            }
        }

        is Action.Logout -> println("Auth started: Logout")
    }
}