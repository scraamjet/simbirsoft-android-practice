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
    private val filterPrefs by lazy { FilterPreferencesManager(requireContext()) }
    private val newsPrefs by lazy { NewsPreferencesManager(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }
    private var newsService: NewsService? = null
    private var bound = false
    private var newsItems: List<NewsItem>? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            newsService = (service as NewsService.LocalBinder).getService()
            bound = true
            loadNewsData()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            bound = false
        }
    }

    override fun onStart() {
        super.onStart()
        requireContext().bindService(
            Intent(requireContext(), NewsService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            requireContext().unbindService(connection)
            bound = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        savedInstanceState?.let { bundle ->
            newsItems =
                BundleCompat.getParcelableArrayList(bundle, KEY_NEWS_ITEMS, NewsItem::class.java)
            newsItems?.let { newsList -> updateNewsList(newsList) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        newsItems?.let { newsList ->
            outState.putParcelableArrayList(
                KEY_NEWS_ITEMS,
                ArrayList(newsList)
            )
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun loadNewsData() {
        binding.progressBarNews.isVisible = true
        newsService?.loadNews { newsList ->
            val filteredNewsItems =
                newsList.filter { newsItem -> newsItem.listHelpCategoryId.any(filterPrefs.getSelectedCategories()::contains) }
                    .map(NewsMapper::toNewsItem)
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
        newsPrefs.saveSelectedNewsId(newsId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        if (bound) loadNewsData()
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
