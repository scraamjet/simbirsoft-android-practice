package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.data.Category
import com.example.simbirsoft_android_practice.utils.generateRandomString
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

private const val CATEGORIES_JSON_FILE = "categories.json"
private const val TIMEOUT_IN_MILLIS = 5_000L
private const val TAG = "CategoryRepository"

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()

    private fun getCategories(): Observable<List<Category>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            gson.fromJson(json, type)
        }
    }

    fun getCombinedCategories(): Observable<Pair<List<Category>, String>> {
        val categoriesObservable = getCategories()
            .doOnSubscribe {
                Log.d(TAG, "Subscribed to categories on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d(TAG, "Emitted categories on thread: ${Thread.currentThread().name}")
            }

        val randomStringObservable = Observable.fromCallable { generateRandomString() }
            .doOnSubscribe {
                Log.d(TAG, "Subscribed to random string on thread: ${Thread.currentThread().name}")
            }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d("CategoryRepository", "Emitted random string on thread: ${Thread.currentThread().name}")
            }

        return Observable.combineLatest(categoriesObservable, randomStringObservable) { categories, randomString ->
            Log.d(TAG, "Combining on thread: ${Thread.currentThread().name}")
            Pair(categories, randomString)
        }
    }

    fun getCombinedCategoriesWithDelay(): Observable<Pair<List<Category>, String>> {
        return getCombinedCategories()
            .delay(TIMEOUT_IN_MILLIS, TimeUnit.MILLISECONDS)
    }

}
