package com.example.simbirsoft_android_practice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HelpAdapter(private val items: List<HelpCategory>) : RecyclerView.Adapter<HelpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_help, parent, false)
        return HelpViewHolder(view)
    }

    override fun onBindViewHolder(holder: HelpViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.title.text = item.title
    }

    override fun getItemCount(): Int = items.size
}