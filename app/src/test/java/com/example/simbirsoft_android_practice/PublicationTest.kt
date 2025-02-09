package com.example.simbirsoft_android_practice

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test

class PublicationTest {
    @Test
    fun testBookAndMagazine() {
        val book1 = Book(15.99, 900)
        val book2 = Book(20.50, 5000)
        val magazine = Magazine(5.99, 12000)

        println(
            ("Book1: Type=${book1.getType()}," +
                    " Words=${book1.wordCount}, Price=%.2f€")
                .format(book1.price)
        )
        println(
            ("Book2: Type=${book2.getType()}, " +
                    "Words=${book2.wordCount}, Price=%.2f€")
                .format(book2.price)
        )
        println(
            ("Magazine: Type=${magazine.getType()}, " +
                    "Words=${magazine.wordCount}, Price=%.2f€")
                .format(magazine.price)
        )

        assertEquals("Flash Fiction", book1.getType())
        assertEquals("Short Story", book2.getType())
        assertEquals("Magazine", magazine.getType())
    }

    @Test
    fun testBookEquality() {
        val book1 = Book(15.99, 900)
        val book2 = Book(15.99, 900)

        assertFalse(book1 === book2)
        assertEquals(book1, book2)
        assertNotEquals(book1, Book(20.50, 5000))
    }

    @Test
    fun testBuyFunction() {
        val book = Book(12.99, 3000)
        val magazine = Magazine(7.50, 10000)

        buy(book)
        buy(magazine)
    }

    @Test
    fun testNullableBooksWithLet() {
        val book1: Book? = null
        val book2: Book? = Book(18.99, 4000)

        book1?.let { buy(it) }
        book2?.let { buy(it) }
    }

    @Test
    fun testLambdaSum() {
        val sum: (Int, Int) -> Unit = { a, b ->
            val result = a + b
            println("Sum of $a and $b is $result")
        }

        sum(5, 10)
        sum(-3, 7)
    }
}