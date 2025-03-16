package com.example.simbirsoft_android_practice.help

import androidx.recyclerview.widget.DiffUtil
import com.example.simbirsoft_android_practice.Category
import com.example.simbirsoft_android_practice.data.HelpCategory

class HelpCategoryDiffCallback : DiffUtil.ItemCallback<HelpCategory>() {
    override fun areItemsTheSame(oldItem: HelpCategory, newItem: HelpCategory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HelpCategory, newItem: HelpCategory): Boolean {
        return oldItem == newItem
    }
}