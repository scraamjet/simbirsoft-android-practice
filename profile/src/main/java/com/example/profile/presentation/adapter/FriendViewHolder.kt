package com.example.profile.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.example.profile.R
import com.example.profile.databinding.ItemFriendBinding
import com.example.profile.presentation.model.Friend

class FriendViewHolder(private val binding: ItemFriendBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Friend) {
        binding.friendName.text = item.name
        binding.friendImage.load(item.imageSrc) {
            placeholder(R.drawable.user_icon)
            error(R.drawable.user_icon)
            memoryCachePolicy(CachePolicy.DISABLED)
            diskCachePolicy(CachePolicy.DISABLED)
            transformations(CircleCropTransformation())
        }
    }
}
