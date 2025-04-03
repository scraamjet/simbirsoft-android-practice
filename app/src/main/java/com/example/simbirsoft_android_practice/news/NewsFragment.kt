package com.example.simbirsoft_android_practice.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
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

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(requireContext())) }
    private val unreadNewsCountSubject = BehaviorSubject.createDefault(0)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        loadNewsData()
    }

    override fun onResume() {
        super.onResume()
        loadNewsData()
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
    private fun loadNewsData() {
        newsRepository.getNews()
            .observeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { newsList ->
                val selectedCategoryIds = filterPrefs.getSelectedCategories()
                val filteredNewsItems = newsList.filter { news ->
                    news.listHelpCategoryId.any { categoryId ->
                        selectedCategoryIds.contains(
                            categoryId
                        )
                    }
                }.map(NewsMapper::toNewsItem)

                newsAdapter.submitList(filteredNewsItems)
                binding.textViewNoNews.isVisible = filteredNewsItems.isEmpty()
                binding.recyclerViewItemNews.isVisible = filteredNewsItems.isNotEmpty()
                updateScrollFlags(filteredNewsItems.isEmpty())
                updateUnreadNewsCount(filteredNewsItems)
            }
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

    private fun updateUnreadNewsCount(newsList: List<NewsItem>) {
        val readNewsIds = newsPrefs.getReadNewsIds()
        val unreadCount = newsList.count { it.id !in readNewsIds }
        unreadNewsCountSubject.onNext(unreadCount)
        (activity as? MainActivity)?.updateUnreadNewsBadge(unreadCount)
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
