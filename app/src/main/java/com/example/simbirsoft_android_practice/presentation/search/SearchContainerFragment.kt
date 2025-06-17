package com.example.simbirsoft_android_practice.presentation.search

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentSearchContainerBinding
import com.example.simbirsoft_android_practice.presentation.main.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

private const val KEYBOARD_VISIBILITY_THRESHOLD_PERCENT = 0.15

class SearchContainerFragment : Fragment(R.layout.fragment_search_container) {

    private val binding by viewBinding(FragmentSearchContainerBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val searchContainerViewModel by viewModels<SearchContainerViewModel> { viewModelFactory }
    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }

    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
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

        binding.viewPagerSearch.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    searchContainerViewModel.onEvent(SearchContainerEvent.OnPageChanged(position = position))
                }
            },
        )
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
                    searchContainerViewModel.onEvent(SearchContainerEvent.OnQueryChanged(query = newText.orEmpty()))
                    return true
                }
            }
        )
    }

    private fun observeKeyboardVisibility() {
        val rootView = binding.root
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            val isKeyboardVisible = keypadHeight > screenHeight * KEYBOARD_VISIBILITY_THRESHOLD_PERCENT

            mainViewModel.setBottomNavigationVisible(!isKeyboardVisible)
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        val rootView = requireActivity().window.decorView.findViewById<View>(android.R.id.content)
        globalLayoutListener?.let { listener ->
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
        globalLayoutListener = null
    }
}
