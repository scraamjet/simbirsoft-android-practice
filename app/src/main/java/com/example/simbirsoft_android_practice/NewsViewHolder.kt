package com.example.simbirsoft_android_practice

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.simbirsoft_android_practice.databinding.ItemNewsBinding

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(news: News) {
        binding.apply {
            textViewNewsTitle.text = news.title
            textViewNewsDescription.text = news.description
            textViewNewsTime.text = DateUtils.formatEventDates(news.startDateTime, news.endDateTime)

            Glide.with(imageViewNewsItem.context)
                .load(news.picturesUrl.firstOrNull())
                .into(imageViewNewsItem)
        }
    }
}