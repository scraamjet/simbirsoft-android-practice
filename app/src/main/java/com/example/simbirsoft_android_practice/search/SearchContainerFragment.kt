package com.example.simbirsoft_android_practice.search

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentSearchContainerBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.ZoomOutPageTransformer
import com.example.simbirsoft_android_practice.utils.findFragmentAtPosition
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private const val DEBOUNCE_DELAY_MILLISECONDS = 500L
private const val KEYBOARD_VISIBILITY_THRESHOLD_PERCENT = 0.15
private const val TAG_SEARCH_CONTAINER_FRAGMENT = "SearchContainerFragment"

class SearchContainerFragment : Fragment(R.layout.fragment_search_container), SearchQueryProvider {
    private val binding by viewBinding(FragmentSearchContainerBinding::bind)

    private val searchQueryFlow = MutableStateFlow("")
    private val supervisorJob = SupervisorJob()
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Log.e(
                TAG_SEARCH_CONTAINER_FRAGMENT,
                "Coroutine exception: ${throwable.localizedMessage}",
                throwable,
            )
        }
    private val coroutineScope =
        CoroutineScope(
            Dispatchers.Main + supervisorJob + coroutineExceptionHandler,
        )

    override fun getSearchQuery(): String = searchQueryFlow.value

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabLayout()
        initSearchView()
        observeKeyboardVisibility()
        observeSearchFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
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
        binding.searchViewSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    searchQueryFlow.value = newText.orEmpty()
                    return true
                }
            },
        )
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchFlow() {
        coroutineScope.launch {
            searchQueryFlow
                .debounce(DEBOUNCE_DELAY_MILLISECONDS)
                .distinctUntilChanged()
                .collectLatest {
                    refreshEventFragment()
                }
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return binding.viewPagerSearch.findFragmentAtPosition(
            childFragmentManager,
            binding.viewPagerSearch.currentItem,
        )
    }

    private fun refreshCurrentFragment() {
        when (val fragment = getCurrentFragment()) {
            is EventListFragment -> {
                fragment.refreshData()
            }

            is OrganizationListFragment -> fragment.refreshData()
        }
    }

    private fun refreshEventFragment() {
        val fragment = getCurrentFragment()
        if (fragment is EventListFragment) {
            fragment.refreshData()
        }
    }

    private fun observeKeyboardVisibility() {
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)

            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            val isKeyboardVisible =
                keypadHeight > screenHeight * KEYBOARD_VISIBILITY_THRESHOLD_PERCENT

            if (isKeyboardVisible) {
                (activity as? MainActivity)?.hideBottomNavigation()
            } else {
                (activity as? MainActivity)?.showBottomNavigation()
            }
        }
    }

    companion object {
        fun newInstance() = SearchContainerFragment()
    }
}
