package com.example.simbirsoft_android_practice

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HelpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.item_help_icon)
    val title: TextView = itemView.findViewById(R.id.item_help_title)
}
