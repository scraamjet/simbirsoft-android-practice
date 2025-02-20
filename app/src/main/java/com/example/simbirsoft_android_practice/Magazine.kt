package com.example.simbirsoft_android_practice

import java.math.BigDecimal

private const val TYPE_MAGAZINE = "Magazine"

class Magazine(
    override val price: BigDecimal,
    override val wordCount: Int
) : Publication {
    override fun getType(): String {
        return TYPE_MAGAZINE
    }

    override fun toString(): String {
        return buildString {
            append("Type: ${getType()}, ")
            append("Words: $wordCount, ")
            append("Price: ${"%.2f".format(price)}€")
        }
    }
}