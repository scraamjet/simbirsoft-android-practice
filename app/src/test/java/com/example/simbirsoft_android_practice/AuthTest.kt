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
    fun testAuthFunction() {
        auth {
            println("Updating cache...")
        }
    }
}