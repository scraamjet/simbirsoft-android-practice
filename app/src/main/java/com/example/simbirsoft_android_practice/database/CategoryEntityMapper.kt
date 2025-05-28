package com.example.simbirsoft_android_practice.database

import com.example.simbirsoft_android_practice.model.Category

object CategoryEntityMapper {
    private fun fromCategory(category: Category): CategoryEntity {
        return CategoryEntity(
            id = category.id,
            nameEn = category.nameEn,
            name = category.name,
            image = category.image,
        )
    }

    private fun toCategory(categoryEntity: CategoryEntity): Category {
        return Category(
            id = categoryEntity.id,
            nameEn = categoryEntity.nameEn,
            name = categoryEntity.name,
            image = categoryEntity.image,
        )
    }

    fun fromCategoryList(categoryList: List<Category>): List<CategoryEntity> {
        return categoryList.map { category -> fromCategory(category) }
    }

    fun toCategoryList(entityList: List<CategoryEntity>): List<Category> {
        return entityList.map { entity -> toCategory(entity) }
    }
}
