package com.example.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.core.model.SearchEvent
import com.example.search.databinding.ItemSearchResultBinding

class EventAdapter : ListAdapter<SearchEvent, EventViewHolder>(SearchEventDiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EventViewHolder {
        val binding =
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int,
    ) {
        val eventItem = getItem(position)
        holder.bind(eventItem)
    }

    companion object {
        private val SearchEventDiffCallback =
            object : DiffUtil.ItemCallback<SearchEvent>() {
                override fun areItemsTheSame(
                    oldItem: SearchEvent,
                    newItem: SearchEvent,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: SearchEvent,
                    newItem: SearchEvent,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
