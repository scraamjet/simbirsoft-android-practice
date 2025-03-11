package com.example.simbirsoft_android_practice.help

import androidx.recyclerview.widget.DiffUtil
import com.example.simbirsoft_android_practice.data.HelpCategory

class HelpCategoryDiffCallback : DiffUtil.ItemCallback<HelpCategory>() {
    override fun areItemsTheSame(oldItem: HelpCategory, newItem: HelpCategory): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: HelpCategory, newItem: HelpCategory): Boolean {
        return oldItem.titleResId == newItem.titleResId && oldItem.icon == newItem.icon
    }
}