package com.example.simbirsoft_android_practice.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.News
import com.example.simbirsoft_android_practice.data.NewsItem
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.main.MainActivity
import com.google.android.material.appbar.AppBarLayout
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject

private const val SCROLL_FLAG_NONE = 0
private const val KEY_NEWS_ITEMS = "news_items"

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(requireContext())) }
    private val unreadNewsCountSubject = BehaviorSubject.createDefault(0)
    private var newsItems: List<NewsItem>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        restoreState(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (newsItems == null) {
            fetchAndShowNews()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
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

    @SuppressLint("CheckResult")
    private fun fetchAndShowNews() {
        showLoading()
        newsRepository.getNews()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { newsList -> filterAndDisplayNews(newsList) }
    }

    private fun filterAndDisplayNews(newsList: List<News>) {
        val selectedCategoryIds = filterPrefs.getSelectedCategories()
        val filteredNewsItems = newsList
            .filter { news -> news.listHelpCategoryId.any { it in selectedCategoryIds } }
            .map(NewsMapper::toNewsItem)

        newsItems = filteredNewsItems
        newsAdapter.submitList(filteredNewsItems)

        binding.apply {
            textViewNoNews.isVisible = filteredNewsItems.isEmpty()
            recyclerViewItemNews.isVisible = filteredNewsItems.isNotEmpty()
            progressBarNews.isVisible = false
        }

        updateScrollFlags(filteredNewsItems.isEmpty())
        updateUnreadNewsCount(filteredNewsItems)
    }

    private fun onNewsItemSelected(newsId: Int) {
        newsPrefs.saveReadNewsId(newsId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
        updateUnreadNewsCount(newsAdapter.currentList)
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

    private fun showData(newsList: List<NewsItem>) {
        newsItems = newsList
        newsAdapter.submitList(newsList)

        binding.apply {
            textViewNoNews.isVisible = newsList.isEmpty()
            recyclerViewItemNews.isVisible = newsList.isNotEmpty()
            progressBarNews.isVisible = false
        }

        updateScrollFlags(newsList.isEmpty())
        updateUnreadNewsCount(newsList)
    }

    private fun showLoading() {
        binding.apply {
            progressBarNews.isVisible = true
            recyclerViewItemNews.isVisible = false
            textViewNoNews.isVisible = false
        }
    }

    private fun updateUnreadNewsCount(newsList: List<NewsItem>) {
        val readNewsIds = newsPrefs.getReadNewsIds()
        val unreadCount = newsList.count { it.id !in readNewsIds }
        unreadNewsCountSubject.onNext(unreadCount)
        (activity as? MainActivity)?.updateUnreadNewsBadge(unreadCount)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.let { bundle ->
            val savedNewsItems =
                BundleCompat.getParcelableArrayList(bundle, KEY_NEWS_ITEMS, NewsItem::class.java)
            savedNewsItems?.let { restoredNewsItems ->
                showData(restoredNewsItems)
                newsItems = restoredNewsItems
            }
        }
    }

    private fun saveState(outState: Bundle) {
        newsItems?.let { savedNewsList ->
            outState.putParcelableArrayList(KEY_NEWS_ITEMS, ArrayList(savedNewsList))
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}

