package com.example.simbirsoft_android_practice.search

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.ItemSearchResultBinding

class EventViewHolder(private val binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(event: Event) {
        binding.itemTitle.text = event.title
    }
}
