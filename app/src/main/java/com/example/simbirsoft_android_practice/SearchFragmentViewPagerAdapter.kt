package com.example.simbirsoft_android_practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchFragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment =
        when (SearchTab.fromPosition(position)) {
            SearchTab.EVENTS -> SearchEventsFragment.newInstance()
            SearchTab.NKO -> SearchNKOFragment.newInstance()
        }

    override fun getItemCount(): Int = SearchTab.count
}