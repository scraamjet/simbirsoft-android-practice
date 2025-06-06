package com.example.simbirsoft_android_practice.core

import android.content.Context
import javax.inject.Inject

class JsonAssetExtractor @Inject constructor(private val context: Context) {
    fun readJsonFile(filename: String): String {
        return context.assets.open(filename).bufferedReader().use { reader ->
            reader.readText()
        }
    }
}
