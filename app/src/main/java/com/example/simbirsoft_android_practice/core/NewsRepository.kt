package com.example.simbirsoft_android_practice.core

import com.example.simbirsoft_android_practice.data.News
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val NEWS_JSON_FILE = "news.json"

class NewsRepository(private val extractor: JsonAssetExtractor) {
    private val gson = Gson()

    fun getNews(): List<News> {
        val json = extractor.readJsonFile(NEWS_JSON_FILE)
        val type = object : TypeToken<List<News>>() {}.type
        return gson.fromJson(json, type)
    }
}
