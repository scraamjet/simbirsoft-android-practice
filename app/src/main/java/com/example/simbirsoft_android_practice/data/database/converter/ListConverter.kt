package com.example.simbirsoft_android_practice.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromListOfStrings(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toListOfStrings(json: String): List<String> {
        return gson.fromJson(json, object : TypeToken<List<String>>() {}.type)
    }
}
