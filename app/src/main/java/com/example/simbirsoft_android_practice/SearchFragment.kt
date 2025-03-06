package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding

private const val EVENTS_TAB_INDEX = 0
private const val NKO_TAB_INDEX = 1

private const val TAB_TITLE_EVENTS = "По мероприятиям"
private const val TAB_TITLE_NKO = "По НКО"

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = binding.searchFragmentViewPager
        viewPager.adapter = SearchFragmentViewPagerAdapter(this)
    }

    private fun initTabLayout() {
        val tabLayout: TabLayout = binding.searchTabLayout
        val viewPager: ViewPager2 = binding.searchFragmentViewPager

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabTitle(position: Int): String =
        when (position) {
            EVENTS_TAB_INDEX -> TAB_TITLE_EVENTS
            NKO_TAB_INDEX -> TAB_TITLE_NKO
            else -> TAB_TITLE_EVENTS
        }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
