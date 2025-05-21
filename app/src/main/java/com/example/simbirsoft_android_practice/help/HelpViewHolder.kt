package com.example.simbirsoft_android_practice.help

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.simbirsoft_android_practice.model.HelpCategory
import com.example.simbirsoft_android_practice.databinding.ItemHelpBinding

class HelpViewHolder(private val binding: ItemHelpBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HelpCategory) {
        binding.textViewItemHelpItem.text = item.title
        binding.imageViewItemHelpIcon.load(item.iconUrl)
    }
}
