package com.example.simbirsoft_android_practice.news

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.NewsItem
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.filter.FilterPreferencesManager
import dev.androidbroadcast.vbpd.viewBinding

private const val KEY_NEWS_ITEMS = "newsItems"

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPreferences by lazy { FilterPreferencesManager(requireContext()) }
    private val newsPreferences by lazy { NewsPreferencesManager(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }
    private var newsService: NewsService? = null
    private var serviceState: NewsServiceState = NewsServiceState.Disconnected
    private var newsItems: List<NewsItem>? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            newsService = (service as NewsService.LocalBinder).getService()
            serviceState = NewsServiceState.Connected
            loadNewsData()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceState = NewsServiceState.Disconnected
            newsService = null
        }
    }

    override fun onStart() {
        super.onStart()
        bindNewsService()
    }

    override fun onStop() {
        super.onStop()
        unbindNewsService()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        restoreNewsItems(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveNewsItems(outState)
    }

    private fun bindNewsService() {
        serviceState = NewsServiceState.Connecting
        requireContext().bindService(
            Intent(requireContext(), NewsService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    private fun unbindNewsService() {
        if (serviceState == NewsServiceState.Connected) {
            requireContext().unbindService(serviceConnection)
            serviceState = NewsServiceState.Disconnected
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun restoreNewsItems(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            val savedNewsItems =
                BundleCompat.getParcelableArrayList(bundle, KEY_NEWS_ITEMS, NewsItem::class.java)
            savedNewsItems?.let { updateNewsList(it) }
        }
    }

    private fun saveNewsItems(outState: Bundle) {
        newsItems?.let { newsList ->
            outState.putParcelableArrayList(KEY_NEWS_ITEMS, ArrayList(newsList))
        }
    }

    private fun loadNewsData() {
        binding.progressBarNews.isVisible = true
        newsService?.loadNews { newsList ->
            val filteredNewsItems = newsList.filter { newsItem ->
                newsItem.listHelpCategoryId.any(filterPreferences.getSelectedCategories()::contains)
            }.map(NewsMapper::toNewsItem)
            updateNewsList(filteredNewsItems)
        }
    }

    private fun updateNewsList(newsList: List<NewsItem>) {
        newsItems = newsList
        binding.apply {
            scrollViewNews.isVisible = newsList.isNotEmpty()
            textViewNoNews.isVisible = newsList.isEmpty()
            progressBarNews.isVisible = false
        }
        newsAdapter.submitList(newsList)
    }

    private fun initClickListeners() {
        binding.imageViewButtonFilters.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, FilterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun onNewsItemSelected(newsId: Int) {
        newsPreferences.saveSelectedNewsId(newsId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if (serviceState == NewsServiceState.Connected) {
            loadNewsData()
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}