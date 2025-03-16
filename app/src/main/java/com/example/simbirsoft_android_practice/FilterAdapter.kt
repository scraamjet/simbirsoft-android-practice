package com.example.simbirsoft_android_practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simbirsoft_android_practice.databinding.ItemFilterBinding

class FilterAdapter : ListAdapter<FilterCategory, FilterViewHolder>(FilterDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)

        holder.binding.itemFilter.setOnCheckedChangeListener(null)
        holder.binding.itemFilter.isChecked = category.isEnabled

        holder.binding.itemFilter.setOnCheckedChangeListener { _, isChecked ->
            category.isEnabled = isChecked
        }
    }
}