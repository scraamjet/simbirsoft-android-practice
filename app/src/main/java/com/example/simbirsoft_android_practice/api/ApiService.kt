package com.example.simbirsoft_android_practice.api

import com.example.simbirsoft_android_practice.data.Category
import com.example.simbirsoft_android_practice.data.Event
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("categories")
    fun getCategories(): Observable<Map<String, Category>>

    @POST("events")
    fun getEvents(@Body categoryId: Map<String, Int>, ): Observable<List<Event>>
}
