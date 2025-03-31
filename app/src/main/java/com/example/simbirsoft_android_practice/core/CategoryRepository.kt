package com.example.simbirsoft_android_practice.core

import android.util.Log
import com.example.simbirsoft_android_practice.data.Category
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val CATEGORIES_JSON_FILE = "categories.json"

class CategoryRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()

    fun getCategories(): Observable<List<Category>> {
        return Observable.fromCallable {
            val json = extractor.readJsonFile(CATEGORIES_JSON_FILE)
            val type = object : TypeToken<List<Category>>() {}.type
            gson.fromJson<List<Category>>(json, type)
        }
            .subscribeOn(Schedulers.io())
            .doOnNext { Log.d("RxJava", "Fetched categories on thread: ${Thread.currentThread().name}") }
    }
}
