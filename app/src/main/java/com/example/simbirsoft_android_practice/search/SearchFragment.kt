package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding

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

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewPager.adapter?.notifyItemChanged(position)
                }
            },
        )
    }

    private fun initTabLayout() {
        val tabLayout: TabLayout = binding.searchTabLayout
        TabLayoutMediator(tabLayout, binding.searchFragmentViewPager) { tab, position ->
            tab.text = SearchTab.fromPosition(position).title
        }.attach()
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
