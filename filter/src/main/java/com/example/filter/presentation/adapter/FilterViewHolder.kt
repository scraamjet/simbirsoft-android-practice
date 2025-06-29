package com.example.filter.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.core.model.FilterCategory
import com.example.filter.databinding.ItemFilterBinding

class FilterViewHolder(val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: FilterCategory) {
        binding.itemFilter.text = category.title
        binding.itemFilter.isChecked = category.isEnabled
    }
}
