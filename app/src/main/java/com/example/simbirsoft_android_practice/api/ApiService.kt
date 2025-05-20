package com.example.simbirsoft_android_practice.api

import com.example.simbirsoft_android_practice.data.Category
import com.example.simbirsoft_android_practice.data.Event
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("categories")
    suspend fun getCategories(): Map<String, Category>

    @POST("events")
    suspend fun getEvents(@Body categoryId: Map<String, Int>): List<Event>
}
