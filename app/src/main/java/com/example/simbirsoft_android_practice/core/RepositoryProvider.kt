package com.example.simbirsoft_android_practice.core

interface RepositoryProvider {
    val newsRepository: NewsRepository
    val categoryRepository: CategoryRepository
}
