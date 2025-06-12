package com.example.simbirsoft_android_practice.search

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentSearchContainerBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.example.simbirsoft_android_practice.utils.ZoomOutPageTransformer
import com.example.simbirsoft_android_practice.utils.findFragmentAtPosition
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class SearchContainerFragment : Fragment(R.layout.fragment_search_container), SearchQueryProvider {

    private val binding by viewBinding(FragmentSearchContainerBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<SearchContainerViewModel> { viewModelFactory }

    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun getSearchQuery(): String = viewModel.searchQuery.value

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initTabLayout()
        initSearchView()
        observeKeyboardVisibility()
    }

    private fun initViewPager() {
        val adapter = SearchViewPagerAdapter(this)
        binding.viewPagerSearch.adapter = adapter
        binding.viewPagerSearch.setPageTransformer(ZoomOutPageTransformer())

        binding.viewPagerSearch.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                refreshCurrentFragment()
            }
        })
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tabLayoutSearch, binding.viewPagerSearch) { tab, position ->
            tab.text = getString(SearchTab.fromPosition(position).titleResId)
        }.attach()
    }

    private fun initSearchView() {
        binding.searchViewSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    private fun refreshCurrentFragment() {
        when (val fragment = getCurrentFragment()) {
            is EventListFragment -> fragment.refreshData(viewModel.debouncedQuery)
            is OrganizationListFragment -> fragment.refreshData()
        }
    }

    private fun getCurrentFragment(): Fragment? {
        return binding.viewPagerSearch.findFragmentAtPosition(
            childFragmentManager,
            binding.viewPagerSearch.currentItem,
        )
    }

    private fun observeKeyboardVisibility() {
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val isKeyboardVisible = keypadHeight > screenHeight * 0.15

            (activity as? MainActivity)?.apply {
                if (isKeyboardVisible) hideBottomNavigation() else showBottomNavigation()
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        globalLayoutListener?.let {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
        globalLayoutListener = null
    }
}
