package com.example.simbirsoft_android_practice.presentation.news

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.simbirsoft_android_practice.databinding.ItemNewsBinding
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.example.simbirsoft_android_practice.core.utils.DateUtils

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(newsItem: NewsItem) {
        binding.apply {
            textViewNewsTitle.text = newsItem.title
            textViewNewsDescription.text = newsItem.description
            textViewNewsTime.text =
                DateUtils.formatEventDates(
                    root.context,
                    newsItem.startDateTime,
                    newsItem.endDateTime,
                )
            imageViewNewsItem.load(newsItem.imageUrl)
        }
    }
}
