package com.example.search

import androidx.recyclerview.widget.RecyclerView
import com.example.core.model.SearchEvent
import com.example.search.databinding.ItemSearchResultBinding

class EventViewHolder(private val binding: ItemSearchResultBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(searchEvent: SearchEvent) {
        binding.textViewItemTitle.text = searchEvent.title
    }
}
