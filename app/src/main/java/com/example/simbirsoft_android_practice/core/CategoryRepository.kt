package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.api.RetrofitClient.apiService
import com.example.simbirsoft_android_practice.data.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

private const val TAG_CATEGORY_REPOSITORY = "CategoryRepository"
private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedCategories: List<Category>? = null

    fun getCategories(): Single<List<Category>> {
        return if (cachedCategories != null) {
            getCategoriesFromCache()
        } else {
            getCategoriesFromRemote()
        }
    }

    private fun getCategoriesFromCache(): Single<List<Category>> {
        val categories = cachedCategories ?: return Single.error(IllegalStateException("Cache is empty"))
        return Single.just(categories)
    }

    private fun getCategoriesFromRemote(): Single<List<Category>> {
        return apiService.getCategories()
            .map { responseMap -> responseMap.values.toList() }
            .doOnSuccess { fetchedCategories -> cachedCategories = fetchedCategories }
            .onErrorResumeNext { error: Throwable ->
                Log.w(
                    TAG_CATEGORY_REPOSITORY,
                    "Remote fetch failed: ${error.message}, loading from storage",
                )
                getCategoriesFromStorage()
            }
    }

    private fun getCategoriesFromStorage(): Single<List<Category>> {
        return Single.fromCallable {
            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            gson.fromJson<List<Category>>(json, type).also { loaded ->
                cachedCategories = loaded
            }
        }.delaySubscription(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }
}
