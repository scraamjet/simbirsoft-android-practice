package com.example.simbirsoft_android_practice.help

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.simbirsoft_android_practice.databinding.ItemHelpBinding
import com.example.simbirsoft_android_practice.model.HelpCategory

class HelpAdapter : ListAdapter<HelpCategory, HelpViewHolder>(HelpCategoryDiffCallback) {
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

    companion object {
        private val HelpCategoryDiffCallback =
            object : DiffUtil.ItemCallback<HelpCategory>() {
                override fun areItemsTheSame(
                    oldItem: HelpCategory,
                    newItem: HelpCategory,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: HelpCategory,
                    newItem: HelpCategory,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
