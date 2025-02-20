package com.example.simbirsoft_android_practice

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test
import java.math.BigDecimal

class PublicationTest {
    @Test
    fun testBookAndMagazine() {
        val book1 = Book(BigDecimal(15.99), 900)
        val book2 = Book(BigDecimal(20.50), 5000)
        val magazine = Magazine(BigDecimal(5.99), 12000)

        println(book1)
        println(book2)
        println(magazine)

        assertEquals("Flash Fiction", book1.getType())
        assertEquals("Short Story", book2.getType())
        assertEquals("Magazine", magazine.getType())
    }

    @Test
    fun testBookEquality() {
        val book1 = Book(BigDecimal(15.99), 900)
        val book2 = Book(BigDecimal(15.99), 900)

        assertFalse(book1 === book2)
        assertEquals(book1, book2)
        assertNotEquals(book1, Book(BigDecimal(20.50), 5000))
    }

    @Test
    fun testBuyFunction() {
        val book = Book(BigDecimal(12.99), 3000)
        val magazine = Magazine(BigDecimal(7.50), 10000)

        buy(book)
        buy(magazine)
    }

    @Test
    fun testNullableBooksWithLet() {
        val book1: Book? = null
        val book2: Book? = Book(BigDecimal(18.99), 4000)

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