package com.example.simbirsoft_android_practice

interface Publication {
    val price: Double
    val wordCount: Int
    fun getType(): String
}