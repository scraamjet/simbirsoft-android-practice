package com.example.simbirsoft_android_practice

import java.math.BigDecimal

private const val FLASH_FICTION_MIN_WORD_COUNT = 1
private const val FLASH_FICTION_MAX_WORD_COUNT = 1000

private const val SHORT_STORY_MIN_WORD_COUNT = 1001
private const val SHORT_STORY_MAX_WORD_COUNT = 7500

private const val TYPE_FLASH_FICTION = "Flash Fiction"
private const val TYPE_SHORT_STORY = "Short Story"
private const val TYPE_NOVEL = "Novel"

class Book(
    override val price: BigDecimal,
    override val wordCount: Int
) : Publication {
    override fun getType(): String {
        return when (wordCount) {
            in FLASH_FICTION_MIN_WORD_COUNT..FLASH_FICTION_MAX_WORD_COUNT -> TYPE_FLASH_FICTION
            in SHORT_STORY_MIN_WORD_COUNT..SHORT_STORY_MAX_WORD_COUNT -> TYPE_SHORT_STORY
            else -> TYPE_NOVEL
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other !is Book) {
            return false
        }
        return price.compareTo(other.price) == 0 && wordCount == other.wordCount
    }

    override fun hashCode(): Int {
        return price.stripTrailingZeros().hashCode() * 31 + wordCount
    }

    override fun toString(): String {
        return buildString {
            append("Type: ${getType()}, ")
            append("Words: $wordCount, ")
            append("Price: ${"%.2f".format(price)}€")
        }
    }
}