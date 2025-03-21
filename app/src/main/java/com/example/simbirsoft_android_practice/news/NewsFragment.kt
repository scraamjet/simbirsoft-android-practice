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
import com.example.simbirsoft_android_practice.utils.updateScrollFlags
import dev.androidbroadcast.vbpd.viewBinding

private const val KEY_NEWS_ITEMS = "newsItems"

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferencesManager(requireContext()) }
    private val newsPrefs by lazy { NewsPreferencesManager(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }

    private var newsService: NewsService? = null
    private var serviceState: NewsServiceState = NewsServiceState.Disconnected
    private var newsItems: List<NewsItem>? = null
    private var isNewsLoaded: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            newsService = (service as NewsService.LocalBinder).getService()
            serviceState = NewsServiceState.Connected

            if (!isNewsLoaded) {
                loadNewsData()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceState = NewsServiceState.Disconnected
        }
    }

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

        savedInstanceState?.let { bundle ->
            val savedNewsItems =
                BundleCompat.getParcelableArrayList(bundle, KEY_NEWS_ITEMS, NewsItem::class.java)
            savedNewsItems?.let { updateNewsList(it) }
            newsItems = savedNewsItems
            isNewsLoaded = bundle.getBoolean("isNewsLoaded", false)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        newsItems?.let { outState.putParcelableArrayList(KEY_NEWS_ITEMS, ArrayList(it)) }
        outState.putBoolean("isNewsLoaded", isNewsLoaded)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isNewsLoaded = false
    }

    private fun initRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun loadNewsData() {
        if (!isAdded || isNewsLoaded) {
            return
        }
        showLoading()
        newsService?.loadNews { newsList ->
            if (!isAdded) {
                return@loadNews
            }
            val filteredNewsItems = newsList.filter {
                it.listHelpCategoryId.any(filterPrefs.getSelectedCategories()::contains)
            }.map(NewsMapper::toNewsItem)

            updateNewsList(filteredNewsItems)
            binding.progressBarNews.isVisible = false
            isNewsLoaded = true
        }
    }

    private fun updateNewsList(newsList: List<NewsItem>) {
        newsItems = newsList
        binding.apply {
            textViewNoNews.isVisible = newsList.isEmpty()
            recyclerViewItemNews.isVisible = newsList.isNotEmpty()
            toolbarNews.updateScrollFlags(newsList.isEmpty())
        }
        newsAdapter.submitList(newsList)
    }

    private fun showLoading() {
        binding.apply {
            recyclerViewItemNews.isVisible = false
            textViewNoNews.isVisible = false
            progressBarNews.isVisible = true
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

    private fun onNewsItemSelected(newsId: Int) {
        newsPrefs.saveSelectedNewsId(newsId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
