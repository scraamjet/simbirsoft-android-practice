package com.example.simbirsoft_android_practice.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.data.HelpCategory
import com.example.simbirsoft_android_practice.databinding.ItemHelpBinding

class HelpAdapter : ListAdapter<HelpCategory, HelpViewHolder>(HelpCategoryDiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): HelpViewHolder {
        val binding = ItemHelpBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HelpViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HelpViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}