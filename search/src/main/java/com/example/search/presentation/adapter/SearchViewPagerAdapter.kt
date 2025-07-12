package com.example.search.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.search.presentation.events.EventListFragment
import com.example.search.presentation.model.SearchTab
import com.example.search.presentation.organizations.OrganizationListFragment

class SearchViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment =
        when (SearchTab.fromPosition(position)) {
            SearchTab.EVENTS -> EventListFragment.newInstance()
            SearchTab.ORGANIZATIONS -> OrganizationListFragment.newInstance()
        }

    override fun getItemCount(): Int = SearchTab.count
}
