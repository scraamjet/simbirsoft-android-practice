package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.databinding.ItemHelpBinding

class HelpViewHolder(private val binding: ItemHelpBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HelpCategory) {
        binding.itemHelpIcon.setImageResource(item.icon)
        binding.itemHelpTitle.text = item.title
    }
}
