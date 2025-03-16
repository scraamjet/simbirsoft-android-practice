package com.example.simbirsoft_android_practice.help

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simbirsoft_android_practice.Category
import com.example.simbirsoft_android_practice.databinding.ItemHelpBinding

class HelpViewHolder(private val binding: ItemHelpBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Category) {
        binding.textViewItemHelpItem.text = item.title
        item.iconUrl?.let {
            Glide.with(binding.imageViewItemHelpIcon.context)
                .load(it)
                .into(binding.imageViewItemHelpIcon)
        }
    }
}