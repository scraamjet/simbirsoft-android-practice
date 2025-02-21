package com.example.simbirsoft_android_practice

import org.junit.Test

class AuthTest {
    @Test
    fun testAuthCallback() {
        val authCallback = object : AuthCallback {
            override fun authSuccess() {
                println("Authentication successful!")
            }

            override fun authFailed() {
                println("Authentication failed!")
            }
        }

        authCallback.authSuccess()
        authCallback.authFailed()
    }
    
    @Test
    fun testAuthCallbackWithAgeCheck() {
        val user = User(1, "Dima", 25, Type.FULL)
        val authCallback = object : AuthCallback {
            override fun authSuccess() {
                println("Authentication successful!")
            }

            override fun authFailed() {
                println("Authentication failed!")
            }
        }
        auth(user, authCallback) {
            println("Updating cache...")
        }
    }

    @Test
    fun testDoActionWithLogin() {
        val user = User(1, "Dima", 25, Type.FULL)
        val authCallback = object : AuthCallback {
            override fun authSuccess() {
                println("Authentication successful!")
            }

            override fun authFailed() {
                println("Authentication failed!")
            }
        }
        doAction(Action.Login(user), authCallback)
    }
}