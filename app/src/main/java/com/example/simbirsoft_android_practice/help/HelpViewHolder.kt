package com.example.simbirsoft_android_practice.help

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.data.HelpCategory
import com.example.simbirsoft_android_practice.databinding.ItemHelpBinding

class HelpViewHolder(private val binding: ItemHelpBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HelpCategory) {
        binding.imageViewItemHelpIcon.setImageResource(item.icon)
        binding.textViewItemHelpItem.setText(item.titleResId)
    }
}
