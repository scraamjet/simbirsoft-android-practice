package com.example.help.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.core.model.HelpCategory
import com.example.help.databinding.ItemHelpBinding

class HelpViewHolder(private val binding: ItemHelpBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: HelpCategory) {
        binding.textViewItemHelpItem.text = item.title
        binding.imageViewItemHelpIcon.load(item.iconUrl)
    }
}
