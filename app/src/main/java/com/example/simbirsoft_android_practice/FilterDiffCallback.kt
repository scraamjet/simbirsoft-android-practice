package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.DiffUtil

class FilterDiffCallback : DiffUtil.ItemCallback<FilterCategory>() {
    override fun areItemsTheSame(oldItem: FilterCategory, newItem: FilterCategory): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: FilterCategory, newItem: FilterCategory): Boolean = oldItem == newItem
}