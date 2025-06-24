package com.example.simbirsoft_android_practice.search

import androidx.recyclerview.widget.RecyclerView
import com.example.simbirsoft_android_practice.databinding.ItemSearchResultBinding
import com.example.simbirsoft_android_practice.domain.model.SearchEvent

class EventViewHolder(private val binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(searchEvent: SearchEvent) {
        binding.textViewItemTitle.text = searchEvent.title
    }
}
