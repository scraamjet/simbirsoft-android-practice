package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.DiffUtil

class NewsDiffCallback : DiffUtil.ItemCallback<NewsItem>() {
    override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean = oldItem == newItem
}