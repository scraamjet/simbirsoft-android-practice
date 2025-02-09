package com.example.simbirsoft_android_practice

import junit.framework.TestCase.assertEquals
import org.junit.Test

class UserTest {
    @Test
    fun testLazyStartTime() {
        val user = User(1, "Dima", 25, Type.FULL)
        val firstTime = user.startTime
        println("First startTime: $firstTime")

        Thread.sleep(1000)

        val secondTime = user.startTime
        println("Second startTime: $secondTime")

        assertEquals("startTime must be immutable", firstTime, secondTime)
    }
}