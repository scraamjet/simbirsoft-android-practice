package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedCategories: List<Category>? = null

    fun hasCachedCategories(): Boolean = cachedCategories != null

    fun getCategoriesFromCache(): Observable<List<Category>> {
        val categories = cachedCategories ?: return Observable.empty()
        return Observable.just(categories)
    }

    fun getCategoriesWithDelay(): Observable<List<Category>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            gson.fromJson<List<Category>>(json, type).also { loadedCategories ->
                cachedCategories = loadedCategories
            }
        }.delaySubscription(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }
}

