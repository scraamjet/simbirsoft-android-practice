package com.example.simbirsoft_android_practice

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class JsonParser(private val context: Context) {

    private val gson = Gson()

    private fun readJsonFile(filename: String): String {
        return context.assets.open(filename).bufferedReader().use { it.readText() }
    }

    fun parseNews(): List<News> {
        val json = readJsonFile("news.json")
        val type = object : TypeToken<List<News>>() {}.type
        return gson.fromJson(json, type)
    }

    fun parseCategories(): List<Category> {
        val json = readJsonFile("categories.json")
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(json, type)
    }
}