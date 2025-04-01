package com.example.simbirsoft_android_practice.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.google.android.material.appbar.AppBarLayout
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random

private const val SCROLL_FLAG_NONE = 0

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val newsPrefs by lazy { NewsPreferences(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(requireContext())) }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
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
        val newsObservable = newsRepository.getNews()
            .observeOn(Schedulers.computation())
            .doOnNext { Log.d("RxJava", "Processing news on thread: ${Thread.currentThread().name}") }
            .observeOn(AndroidSchedulers.mainThread())

        val randomObservable = Observable.fromCallable {
            List(10) { Random.nextInt(100) }
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .doOnNext { Log.d("RxJava", "Generated random data on thread: ${Thread.currentThread().name}") }

        Observable.zip(newsObservable, randomObservable) { newsList, _ ->
            Log.d("RxJava", "Combined on thread: ${Thread.currentThread().name}")
            newsList
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { newsList ->
                val selectedCategoryIds = filterPrefs.getSelectedCategories()
                val filteredNewsItems =
                    newsList
                        .filter { news ->
                            news.listHelpCategoryId.any { categoryId ->
                                selectedCategoryIds.contains(categoryId)
                            }
                        }
                        .map(NewsMapper::toNewsItem)
                newsAdapter.submitList(filteredNewsItems)
                binding.apply {
                    textViewNoNews.isVisible = filteredNewsItems.isEmpty()
                    recyclerViewItemNews.isVisible = filteredNewsItems.isNotEmpty()
                    updateScrollFlags(filteredNewsItems.isEmpty())
                }
            }
    }

    private fun onNewsItemSelected(newsId: Int) {
        newsPrefs.saveSelectedNewsId(newsId)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
            .addToBackStack(null)
            .commit()
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
