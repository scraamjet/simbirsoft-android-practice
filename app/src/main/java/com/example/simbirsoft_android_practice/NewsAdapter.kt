package com.example.simbirsoft_android_practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.simbirsoft_android_practice.databinding.ItemNewsBinding

class NewsAdapter(private val onItemClicked: (idItem: Int) -> Unit) :
    ListAdapter<NewsItem, NewsViewHolder>(NewsDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        holder.bind(newsItem)
        holder.itemView.setOnClickListener { onItemClicked(newsItem.id) }
    }

    companion object {
        private val NewsDiffCallback = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}