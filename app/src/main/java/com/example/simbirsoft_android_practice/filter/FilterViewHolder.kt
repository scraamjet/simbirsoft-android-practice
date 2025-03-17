package com.example.simbirsoft_android_practice.filter

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.databinding.ItemFilterBinding

class FilterViewHolder(val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: FilterCategory) {
        binding.itemFilter.text = category.title
        binding.itemFilter.isChecked = category.isEnabled
    }
}