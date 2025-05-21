package com.example.simbirsoft_android_practice.search

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.model.SearchEvent
import com.example.simbirsoft_android_practice.databinding.ItemSearchResultBinding

class EventViewHolder(private val binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(searchEvent: SearchEvent) {
        binding.textViewItemTitle.text = searchEvent.title
    }
}
