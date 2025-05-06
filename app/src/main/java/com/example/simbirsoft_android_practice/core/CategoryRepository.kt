package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.api.RetrofitClient.apiService
import com.example.simbirsoft_android_practice.data.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

private const val TAG_CATEGORY_REPOSITORY = "CategoryRepository"
private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()
    private var cachedCategories: List<Category>? = null

    fun getCategoriesFlow(): Flow<List<Category>> {
        return if (cachedCategories != null) {
            getCategoriesFromCacheFlow()
        } else {
            getCategoriesFromRemoteFlow()
        }
    }

    private fun getCategoriesFromRemoteFlow(): Flow<List<Category>> =
        flow {
            val responseMap = apiService.getCategories().blockingFirst()
            val fetchedCategories = responseMap.values.toList()
            cachedCategories = fetchedCategories
            emit(fetchedCategories)
        }.catch { error ->
            Log.w(TAG_CATEGORY_REPOSITORY, "Remote fetch failed: ${error.message}, loading from storage")
            emitAll(getCategoriesFromStorageFlow())
        }

    private fun getCategoriesFromCacheFlow(): Flow<List<Category>> = flowOf(cachedCategories ?: error("Category cache is empty"))

    private fun getCategoriesFromStorageFlow(): Flow<List<Category>> =
        flow {
            delay(TIMEOUT_IN_MILLIS)
            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            gson.fromJson<List<Category>>(json, type).also { loadedCategories ->
                cachedCategories = loadedCategories
                emit(loadedCategories)
            }
        }
}
