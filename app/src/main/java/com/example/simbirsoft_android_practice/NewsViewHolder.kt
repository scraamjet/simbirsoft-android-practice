package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simbirsoft_android_practice.databinding.ItemNewsBinding
import com.example.simbirsoft_android_practice.utils.DateUtils

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(newsItem: NewsItem) {
        binding.apply {
            textViewNewsTitle.text = newsItem.title
            textViewNewsDescription.text = newsItem.description
            textViewNewsTime.text = DateUtils.formatEventDates(newsItem.startDateTime, newsItem.endDateTime)

            Glide.with(imageViewNewsItem.context)
                .load(newsItem.imageUrl)
                .into(imageViewNewsItem)
        }
    }
}