package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.DiffUtil

class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem
}