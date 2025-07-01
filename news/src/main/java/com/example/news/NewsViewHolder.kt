package com.example.news

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.core.model.NewsItem
import com.example.news.databinding.ItemNewsBinding

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
