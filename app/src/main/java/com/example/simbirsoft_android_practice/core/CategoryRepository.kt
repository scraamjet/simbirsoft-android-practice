package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.api.RetrofitClient.apiService
import com.example.simbirsoft_android_practice.database.CategoryDao
import com.example.simbirsoft_android_practice.database.CategoryEntityMapper
import com.example.simbirsoft_android_practice.model.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TAG_CATEGORY_REPOSITORY = "CategoryRepository"
private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class CategoryRepository(
    private val extractor: JsonAssetExtractor,
    private val categoryDao: CategoryDao,
) {
    private val gson = Gson()
    private var isInitialized = false

    fun getCategories(): Flow<List<Category>> {
        return if (isInitialized) {
            getCategoriesFromDatabase()
        } else {
            isInitialized = true
            getCategoriesFromRemote()
        }
    }

    private fun getCategoriesFromRemote(): Flow<List<Category>> =
        flow {
            val responseMap = apiService.getCategories()
            val fetchedCategories = responseMap.values.toList()

            val categoryEntities = CategoryEntityMapper.fromCategoryList(fetchedCategories)
            categoryDao.insertAllCategories(categoryEntities)

            emit(fetchedCategories)
        }.catch { throwable ->
            Log.w(
                TAG_CATEGORY_REPOSITORY,
                "Remote fetch failed: ${throwable.message}, loading from storage"
            )
            emitAll(getCategoriesFromJson())
        }

    private fun getCategoriesFromJson(): Flow<List<Category>> =
        flow {
            delay(TIMEOUT_IN_MILLIS)

            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            val loadedCategories = gson.fromJson<List<Category>>(json, type)

            val categoryEntities = CategoryEntityMapper.fromCategoryList(loadedCategories)
            categoryDao.insertAllCategories(categoryEntities)

            emit(loadedCategories)
        }

    private fun getCategoriesFromDatabase(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            CategoryEntityMapper.toCategoryList(entities)
        }
    }
}

