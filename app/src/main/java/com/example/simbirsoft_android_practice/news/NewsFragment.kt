package com.example.simbirsoft_android_practice.news

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.main.MainViewModel
import com.example.simbirsoft_android_practice.domain.model.NewsItem
import com.google.android.material.appbar.AppBarLayout
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SCROLL_FLAG_NONE = 0

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val mainViewModel: MainViewModel by activityViewModels { viewModelFactory }
    private val newsViewModel: NewsViewModel by viewModels { viewModelFactory }

    private val newsAdapter by lazy {
        NewsAdapter { newsItemId -> onNewsItemClicked(newsId = newsItemId) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        observeUiState()
        observeEffects()
    }

    private fun initRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun initClickListeners() {
        binding.imageViewButtonFilters.setOnClickListener {
            findNavController().navigate(R.id.action_news_to_filter)
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.uiState.collect { state: NewsState ->
                    when (state) {
                        is NewsState.Loading -> showLoading()
                        is NewsState.Results -> showResults(newsList = state.newsList)
                        is NewsState.NoResults -> showNoResults()
                        is NewsState.Error -> showError()
                    }
                }
            }
        }
    }

    private fun observeEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsViewModel.effect.collect { effect: NewsEffect ->
                    when (effect) {
                        is NewsEffect.NavigateToNewsDetail -> navigateToNewsDetail(newsId = effect.newsId)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBarNews.isVisible = true
        binding.recyclerViewItemNews.isVisible = false
        binding.textViewNoNews.isVisible = false
    }

    private fun showResults(newsList: List<NewsItem>) {
        binding.progressBarNews.isVisible = false
        binding.recyclerViewItemNews.isVisible = true
        binding.textViewNoNews.isVisible = false

        newsAdapter.submitList(newsList)
        updateScrollFlags(isListEmpty = false)

        mainViewModel.updateBadgeCount(newsItems = newsList)
    }

    private fun showNoResults() {
        binding.progressBarNews.isVisible = false
        binding.recyclerViewItemNews.isVisible = false
        binding.textViewNoNews.isVisible = true

        updateScrollFlags(isListEmpty = true)
        mainViewModel.updateBadgeCount(newsItems = emptyList())
    }

    private fun showError() {
        binding.progressBarNews.isVisible = false
        binding.recyclerViewItemNews.isVisible = false
        binding.textViewNoNews.isVisible = true

        updateScrollFlags(isListEmpty = true)
        mainViewModel.updateBadgeCount(newsItems = emptyList())
    }

    private fun onNewsItemClicked(newsId: Int) {
        mainViewModel.updateReadNews(newsId = newsId)
        newsViewModel.onEvent(NewsEvent.NewsClicked(newsId = newsId))
    }

    private fun navigateToNewsDetail(newsId: Int) {
        val action = NewsFragmentDirections.actionNewsToNewsDetail(newsId = newsId)
        findNavController().navigate(action)
    }

    private fun updateScrollFlags(isListEmpty: Boolean) {
        (binding.toolbarNews.layoutParams as AppBarLayout.LayoutParams).apply {
            scrollFlags = if (isListEmpty) {
                SCROLL_FLAG_NONE
            } else {
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            }
        }
    }
}

