package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.concurrent.Executors

private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private val executor = Executors.newSingleThreadExecutor()


    fun getCategories(): List<Category> {
        val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
        val type = object : TypeToken<List<Category>>() {}.type
        return gson.fromJson(json, type)
    }

    fun getCategoriesAsync(onCategoriesLoaded: (List<Category>?) -> Unit) {
        executor.execute {
            Thread.sleep(TIMEOUT_IN_MILLIS)
            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            val categories: List<Category>? = gson.fromJson(json, type)
            onCategoriesLoaded(categories)
        }
    }

    fun releaseResources() {
        executor.shutdown()
    }
}
