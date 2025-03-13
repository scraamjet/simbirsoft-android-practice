package com.example.simbirsoft_android_practice

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonParser(private val context: Context) {

    private val gson = Gson()

    private fun readJsonFile(filename: String): String {
        return context.assets.open(filename).bufferedReader().use { it.readText() }
    }

    fun parseNews(fileName: String): List<News> {
        val json = readJsonFile(fileName)
        val type = object : TypeToken<List<News>>() {}.type
        return gson.fromJson(json, type)
    }

    fun parseCategories(fileName: String): List<Category> {
        val json = readJsonFile(fileName)
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(json, type)
    }
}