package com.example.simbirsoft_android_practice.news

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.simbirsoft_android_practice.data.NewsItem
import com.example.simbirsoft_android_practice.databinding.ItemNewsBinding
import com.example.simbirsoft_android_practice.utils.DateUtils

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(newsItem: NewsItem) {
        binding.apply {
            textViewNewsTitle.text = newsItem.title
            textViewNewsDescription.text = newsItem.description
            textViewNewsTime.text =
                DateUtils.formatEventDates(newsItem.startDateTime, newsItem.endDateTime)

            imageViewNewsItem.load(newsItem.imageUrl)
        }
    }
}