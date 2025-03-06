package com.example.simbirsoft_android_practice

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val PAGER_EVENTS_INDEX = 0
private const val PAGER_NKO_INDEX = 1
private const val TOTAL_TABS_COUNT = 2

class SearchFragmentViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            PAGER_EVENTS_INDEX -> SearchEventsFragment.newInstance()
            PAGER_NKO_INDEX -> SearchNKOFragment.newInstance()
            else -> SearchEventsFragment.newInstance()
        }

    override fun getItemCount() = TOTAL_TABS_COUNT
}
