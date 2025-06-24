package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.data.api.ApiService
import com.example.simbirsoft_android_practice.data.database.dao.CategoryDao
import com.example.simbirsoft_android_practice.data.database.mapper.CategoryEntityMapper
import com.example.simbirsoft_android_practice.domain.model.Category
import com.example.simbirsoft_android_practice.domain.repository.CategoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val TAG_CATEGORY_REPOSITORY = "CategoryRepository"
private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L

class CategoryRepositoryImpl @Inject constructor(
    private val extractor: JsonAssetExtractor,
    private val categoryDao: CategoryDao,
    private val gson: Gson,
    private val apiService: ApiService,
    ) : CategoryRepository {
        private var isDataLoaded = false

    override fun getCategories(): Flow<List<Category>> {
            return if (isDataLoaded) {
                getCategoriesFromDatabase()
            } else {
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

                isDataLoaded = true
            }.catch { throwable ->
                Log.w(
                    TAG_CATEGORY_REPOSITORY,
                    "Remote fetch failed: ${throwable.message}, loading from storage",
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

                isDataLoaded = true
            }

        private fun getCategoriesFromDatabase(): Flow<List<Category>> {
            return categoryDao.getAllCategories().map { entities ->
                CategoryEntityMapper.toCategoryList(entities)
            }
        }
    }
