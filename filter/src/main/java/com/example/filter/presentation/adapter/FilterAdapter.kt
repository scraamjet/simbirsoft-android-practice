package com.example.filter.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.core.model.FilterCategory
import com.example.filter.databinding.ItemFilterBinding

class FilterAdapter : ListAdapter<FilterCategory, FilterViewHolder>(FilterDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FilterViewHolder,
        position: Int,
    ) {
        val category = getItem(position)
        holder.bind(category)

        holder.binding.itemFilter.isChecked = category.isEnabled

        holder.binding.itemFilter.setOnCheckedChangeListener { _, isChecked ->
            category.isEnabled = isChecked
        }
    }

    companion object {
        private val FilterDiffCallback =
            object : DiffUtil.ItemCallback<FilterCategory>() {
                override fun areItemsTheSame(
                    oldItem: FilterCategory,
                    newItem: FilterCategory,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: FilterCategory,
                    newItem: FilterCategory,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
