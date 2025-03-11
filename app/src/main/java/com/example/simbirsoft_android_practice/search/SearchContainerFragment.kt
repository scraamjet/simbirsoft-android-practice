package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentSearchContainerBinding
import com.example.simbirsoft_android_practice.utils.ZoomOutPageTransformer
import com.example.simbirsoft_android_practice.utils.findFragmentAtPosition
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding

class SearchContainerFragment : Fragment(R.layout.fragment_search_container) {
    private val binding by viewBinding(FragmentSearchContainerBinding::bind)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabLayout()
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = binding.viewPager
        val adapter = SearchContainerFragmentViewPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.setPageTransformer(ZoomOutPageTransformer())

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    refreshCurrentFragment(position)
                }
            },
        )
    }

    private fun initTabLayout() {
        val tabLayout: TabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
            tab.text = getString(SearchTab.fromPosition(position).titleResId)
        }.attach()
    }

    private fun refreshCurrentFragment(position: Int) {
        val fragment =
            binding.viewPager.findFragmentAtPosition(
                childFragmentManager,
                position,
            )
        when (fragment) {
            is EventListFragment -> fragment.refreshData()
            is OrganizationListFragment -> fragment.refreshData()
        }
    }

    companion object {
        fun newInstance() = SearchContainerFragment()
    }
}
