package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.DiffUtil

class NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem
}