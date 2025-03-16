package com.example.simbirsoft_android_practice.help

import androidx.recyclerview.widget.DiffUtil
import com.example.simbirsoft_android_practice.Category
import com.example.simbirsoft_android_practice.data.HelpCategory

class HelpCategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}
