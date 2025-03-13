package com.example.simbirsoft_android_practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.simbirsoft_android_practice.databinding.ItemFilterBinding

class CategoryAdapter : ListAdapter<Category, CategoryViewHolder>(CategoryDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.bind(category)

        holder.binding.itemFilter.setOnCheckedChangeListener(null)
        holder.binding.itemFilter.isChecked = category.isEnabled

        holder.binding.itemFilter.setOnCheckedChangeListener { _, isChecked ->
            category.isEnabled = isChecked
        }
    }
}