package com.example.simbirsoft_android_practice

import java.math.BigDecimal

interface Publication {
    val price: BigDecimal
    val wordCount: Int
    fun getType(): String
}