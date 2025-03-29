package com.example.simbirsoft_android_practice.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentSearchContainerBinding
import com.example.simbirsoft_android_practice.utils.ZoomOutPageTransformer
import com.example.simbirsoft_android_practice.utils.findFragmentAtPosition
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val DEBOUNCE_DELAY_MILLISECONDS = 500L

class SearchContainerFragment : Fragment(R.layout.fragment_search_container) {

    private val binding by viewBinding(FragmentSearchContainerBinding::bind)
    private val searchSubject = PublishSubject.create<String>()
    private var currentQuery: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabLayout()
        initSearchView()
    }

    private fun initViewPager() {
        val viewPager: ViewPager2 = binding.viewPagerSearch
        val adapter = SearchViewPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.setPageTransformer(ZoomOutPageTransformer())

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    refreshCurrentFragment()
                }
            },
        )
    }

    private fun initTabLayout() {
        val tabLayout: TabLayout = binding.tabLayoutSearch
        TabLayoutMediator(tabLayout, binding.viewPagerSearch) { tab, position ->
            tab.text = getString(SearchTab.fromPosition(position).titleResId)
        }.attach()
    }

    @SuppressLint("CheckResult")
    private fun initSearchView() {

        binding.searchViewSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentQuery = newText.orEmpty()
                searchSubject.onNext(currentQuery)
                return true
            }
        })

        searchSubject
            .debounce(DEBOUNCE_DELAY_MILLISECONDS, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { refreshCurrentFragment() }
    }

    private fun refreshCurrentFragment() {
        val fragment = binding.viewPagerSearch.findFragmentAtPosition(
            childFragmentManager,
            binding.viewPagerSearch.currentItem
        )
        if (fragment is EventListFragment) {
            fragment.searchQuery = currentQuery
            fragment.refreshData()
        }
    }

    companion object {
        fun newInstance() = SearchContainerFragment()
    }
}
