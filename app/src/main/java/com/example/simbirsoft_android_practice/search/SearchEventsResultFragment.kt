package com.example.simbirsoft_android_practice.search

import com.example.simbirsoft_android_practice.data.Event

class SearchEventsResultFragment : SearchResultFragment() {
    override val itemCount: Int = 5

    override fun generateEventsList(): List<Event> {
        return List(itemCount) { Event(generateRandomString()) }
    }

    companion object {
        fun newInstance() = SearchEventsResultFragment()
    }
}