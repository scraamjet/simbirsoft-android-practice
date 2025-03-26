package com.example.simbirsoft_android_practice.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.NewsItem
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.google.android.material.appbar.AppBarLayout
import dev.androidbroadcast.vbpd.viewBinding

private const val KEY_NEWS_ITEMS = "news_items"
private const val SCROLL_FLAG_NONE = 0

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }
    private var newsService: NewsService? = null
    private var serviceState: NewsServiceState = NewsServiceState.Disconnected
    private var newsItems: List<NewsItem>? = null

    private val connection =
        NewsServiceConnection(
            onServiceConnected = { connectedService ->
                newsService = connectedService
                serviceState = NewsServiceState.Connected
                if (newsItems == null) {
                    loadNewsData()
                }
            },
            onServiceDisconnected = {
                serviceState = NewsServiceState.Disconnected
            }
        )

    override fun onStart() {
        super.onStart()
        serviceState = NewsServiceState.Connecting
        requireContext().bindService(
            Intent(requireContext(), NewsService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (serviceState == NewsServiceState.Connected) {
            requireContext().unbindService(connection)
            serviceState = NewsServiceState.Disconnected
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newsItems = null
    }

    private fun initRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun initClickListeners() {
        binding.imageViewButtonFilters.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, FilterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadNewsData() {
        showLoading()
        newsService?.loadNews { loadedNewsList ->
            if (isAdded) {
                val selectedCategories = filterPrefs.getSelectedCategories()
                val filteredNewsItems =
                    loadedNewsList.filter { newsItem ->
                        newsItem.listHelpCategoryId.any { categoryId ->
                            selectedCategories.contains(categoryId)
                        }
                    }.map(NewsMapper::toNewsItem)
                newsItems = filteredNewsItems
                showData(filteredNewsItems)
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            recyclerViewItemNews.isVisible = false
            textViewNoNews.isVisible = false
            progressBarNews.isVisible = true
        }
    }

    private fun showData(newsList: List<NewsItem>) {
        binding.apply {
            textViewNoNews.isVisible = newsList.isEmpty()
            recyclerViewItemNews.isVisible = newsList.isNotEmpty()
            updateScrollFlags(newsList.isEmpty())
            progressBarNews.isVisible = false
        }
        newsAdapter.submitList(newsList)
    }

    private fun onNewsItemSelected(newsId: Int) {
        newsPrefs.saveSelectedNewsId(newsId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            val savedNewsItems =
                BundleCompat.getParcelableArrayList(bundle, KEY_NEWS_ITEMS, NewsItem::class.java)
            savedNewsItems?.let { restoredNewsItems -> showData(restoredNewsItems) }
            newsItems = savedNewsItems
        }
    }

    private fun saveState(outState: Bundle) {
        newsItems?.let { savedNewsList ->
            outState.putParcelableArrayList(KEY_NEWS_ITEMS, ArrayList(savedNewsList))
        }
    }

    private fun updateScrollFlags(isListEmpty: Boolean) {
        (binding.toolbarNews.layoutParams as AppBarLayout.LayoutParams).apply {
            scrollFlags =
                if (isListEmpty) {
                    SCROLL_FLAG_NONE
                } else {
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                            AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                            AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                }
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}


