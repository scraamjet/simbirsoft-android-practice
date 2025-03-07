package com.example.simbirsoft_android_practice.search

enum class SearchTab(val title: String) {
    EVENTS("По мероприятиям"),
    NKO("По НКО");

    companion object {
        fun fromPosition(position: Int): SearchTab = entries.toTypedArray().getOrElse(position) {
            EVENTS
        }

        val count: Int get() = entries.size
    }
}