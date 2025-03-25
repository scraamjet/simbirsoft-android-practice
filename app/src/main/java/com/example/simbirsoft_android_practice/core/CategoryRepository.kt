package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val CATEGORIES_JSON_FILE = "categories.json"

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()

    fun getCategories(): List<Category> {
        val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(json, type)
    }
}
