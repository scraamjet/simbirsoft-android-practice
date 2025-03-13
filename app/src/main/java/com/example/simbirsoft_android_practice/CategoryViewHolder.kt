package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.databinding.ItemFilterBinding

class CategoryViewHolder(private val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: Category) {
        binding.itemFilter.text = category.title
    }
}