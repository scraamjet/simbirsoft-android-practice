package com.example.simbirsoft_android_practice.search

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit

private const val DEBOUNCE_DELAY_MILLISECONDS = 500L
private const val KEYBOARD_VISIBILITY_THRESHOLD_PERCENT = 0.15

class SearchContainerFragment : Fragment(R.layout.fragment_search_container), SearchQueryProvider {
    private val binding by viewBinding(FragmentSearchContainerBinding::bind)
    private val searchSubject = PublishSubject.create<String>()
    private val compositeDisposable = CompositeDisposable()
    private var currentQuery: String = ""
    override fun getSearchQuery(): String = currentQuery


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

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
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
                    currentQuery = newText.orEmpty()
                    searchSubject.onNext(currentQuery)
                    return true
                }
            },
        )

          searchSubject
              .debounce(DEBOUNCE_DELAY_MILLISECONDS, TimeUnit.MILLISECONDS)
              .distinctUntilChanged()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe { refreshEventFragment() }
              .let(compositeDisposable::add)
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
