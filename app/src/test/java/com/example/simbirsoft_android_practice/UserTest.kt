package com.example.simbirsoft_android_practice

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test

class UserTest {
    private val users: MutableList<User> = mutableListOf(User(1, "Dima", 25, Type.FULL)).apply {
        add(User(2, "Artem", 22, Type.DEMO))
        add(User(3, "Kate", 28, Type.FULL))
        add(User(4, "John", 35, Type.DEMO))
        add(User(5, "Emma", 22, Type.FULL))
    }

    @Test
    fun testUsersListIsNotEmpty() {
        assertTrue("Users list should not be empty", users.isNotEmpty())
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
    fun testFilterUsersWithFullAccess() {
        val fullAccessUsers = users.filter { user -> user.type == Type.FULL }
        println("Users with FULL access: $fullAccessUsers")
        assertTrue(
            "All filtered users must have FULL access",
            fullAccessUsers.all { user -> user.type == Type.FULL }
        )
    }

    @Test
    fun testTransformUsersToNames() {
        val userNames = users.map { user -> user.name }
        val firstUserName = userNames.first()
        val lastUserName = userNames.last()

        println("First user: $firstUserName")
        println("Last user: $lastUserName")

        assertEquals("First user must be Dima", firstUserName, "Dima")
        assertEquals("Last user must be Emma", lastUserName, "Emma")
    }

    @Test
    fun testIsAdultFunction() {
        val adultUser = User(1, "Dima", 25, Type.FULL)
        val minorUser = User(2, "Leo", 16, Type.DEMO)

        val adultCheck = runCatching {
            adultUser.throwIfUnderage()
        }
        val minorCheck = runCatching {
            minorUser.throwIfUnderage()
        }

        assertTrue("Dima must be adult", adultCheck.isSuccess)
        assertTrue("Leo must not be adult", minorCheck.isFailure)
    }
}