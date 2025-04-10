package com.example.simbirsoft_android_practice.search

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
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

private const val DEBOUNCE_DELAY_MILLISECONDS = 500L
private const val KEYBOARD_VISIBILITY_THRESHOLD_PERCENT = 0.15
private const val TAG = "SearchContainerFragment"

class SearchContainerFragment : Fragment(R.layout.fragment_search_container) {
    private val binding by viewBinding(FragmentSearchContainerBinding::bind)
    private val searchQueryFlow = MutableStateFlow("")
    private var currentQuery: String = ""

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

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
        job.cancel()
    }

    private fun initViewPager() {
        val adapter = SearchViewPagerAdapter(this)
        binding.viewPagerSearch.apply {
            this.adapter = adapter
            setPageTransformer(ZoomOutPageTransformer())
            registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        refreshCurrentFragment()
                    }
                },
            )
        }
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayoutSearch, binding.viewPagerSearch) { tab, position ->
            tab.text = getString(SearchTab.fromPosition(position).titleResId)
        }.attach()
    }

    private fun initSearchView() {
        binding.searchViewSearch.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    val query = newText.orEmpty()
                    currentQuery = query
                    searchQueryFlow.value = query
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
                .flowOn(Dispatchers.Default)
                .catch { exception ->
                    Log.e(
                        TAG,
                        "Error while processing search query: ${exception.localizedMessage}",
                    )
                }
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
                fragment.searchQuery = currentQuery
                fragment.refreshData()
            }

            is OrganizationListFragment -> fragment.refreshData()
        }
    }

    private fun refreshEventFragment() {
        val fragment = getCurrentFragment()
        if (fragment is EventListFragment) {
            fragment.searchQuery = currentQuery
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
