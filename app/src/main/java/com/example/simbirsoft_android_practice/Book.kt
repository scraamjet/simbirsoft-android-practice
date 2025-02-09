package com.example.simbirsoft_android_practice

class Book(
    override val price: Double,
    override val wordCount: Int
) : Publication {
    override fun getType(): String {
        return if (wordCount <= 1000) {
            "Flash Fiction"
        } else if (wordCount <= 7500) {
            "Short Story"
        } else {
            "Novel"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Book) {
            return false
        }
        return price == other.price && wordCount == other.wordCount
    }

    override fun hashCode(): Int {
        return price.hashCode() * 31 + wordCount
    }
}