package com.example.simbirsoft_android_practice.presentation.filter

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.databinding.ItemFilterBinding
import com.example.simbirsoft_android_practice.domain.model.FilterCategory

class FilterViewHolder(val binding: ItemFilterBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(category: FilterCategory) {
        binding.itemFilter.text = category.title
        binding.itemFilter.isChecked = category.isEnabled
    }
}
