package com.example.simbirsoft_android_practice

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class UserTest {
    private lateinit var users: MutableList<User>

    @Before
    fun setup() {
        users = mutableListOf(User(1, "Dima", 25, Type.FULL)).apply {
            add(User(2, "Artem", 22, Type.DEMO))
            add(User(3, "Kate", 28, Type.FULL))
            add(User(4, "John", 35, Type.DEMO))
            add(User(5, "Emma", 22, Type.FULL))
        }
    }

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

    @Test
    fun testFilterUsersWithFulLAccess() {
        val fullAccessUsers = users.filter { it.type == Type.FULL }
        println("Users with FULL access: $fullAccessUsers")
        assertTrue(
            "All filtered users must have FULL access",
            fullAccessUsers.all { it.type == Type.FULL },
        )
    }

    @Test
    fun testTransformUsersToNames() {
        val userNames = users.map { it.name }
        val firstUser = userNames.first()
        val lastUser = userNames.last()

        println("First user: $firstUser")
        println("Last user: $lastUser")

        assertEquals("First user must be Dima", firstUser, "Dima")
        assertEquals("Last user must be Emma", lastUser, "Emma")
    }

    @Test
    fun testCheckAdultFunction() {
        val adultUser = User(1, "Dima", 25, Type.FULL)
        val minorUser = User(2, "Leo", 16, Type.DEMO)

        assertTrue("Dima must be adult", adultUser.checkAdult().isSuccess)
        assertTrue("Leo must not be adult", minorUser.checkAdult().isFailure)
    }
}