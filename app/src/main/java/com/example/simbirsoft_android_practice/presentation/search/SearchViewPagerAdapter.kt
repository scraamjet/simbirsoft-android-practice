package com.example.simbirsoft_android_practice.presentation.search

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SearchViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment =
        when (SearchTab.fromPosition(position)) {
            SearchTab.EVENTS -> EventListFragment.newInstance()
            SearchTab.ORGANIZATIONS -> OrganizationListFragment.newInstance()
        }

    override fun getItemCount(): Int = SearchTab.count
}
