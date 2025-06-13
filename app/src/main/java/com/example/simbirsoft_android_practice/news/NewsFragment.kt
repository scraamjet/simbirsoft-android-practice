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
import com.example.simbirsoft_android_practice.model.NewsItem
import com.google.android.material.appbar.AppBarLayout
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SCROLL_FLAG_NONE = 0

class NewsFragment : Fragment(R.layout.fragment_news) {
    private val binding by viewBinding(FragmentNewsBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val mainViewModel by activityViewModels<MainViewModel> { viewModelFactory }
    private val newsViewModel by viewModels<NewsViewModel> { viewModelFactory }

    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        observeNews()
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

    private fun observeNews() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    newsViewModel.uiState.collect { state ->
                        when (state) {
                            is NewsUiState.Loading -> showLoading()
                            is NewsUiState.Results -> showResults(state.news)
                            is NewsUiState.NoResults -> showNoResults()
                            is NewsUiState.Error -> showError()
                        }
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

        mainViewModel.updateBadge(newsList)
    }

    private fun showNoResults() {
        binding.progressBarNews.isVisible = false
        binding.recyclerViewItemNews.isVisible = false
        binding.textViewNoNews.isVisible = true

        updateScrollFlags(isListEmpty = true)
        mainViewModel.updateBadge(emptyList())
    }

    private fun showError() {
        binding.progressBarNews.isVisible = false
        binding.recyclerViewItemNews.isVisible = false
        binding.textViewNoNews.isVisible = true

        updateScrollFlags(isListEmpty = true)
        mainViewModel.updateBadge(emptyList())
    }

    private fun onNewsItemSelected(newsId: Int) {
        mainViewModel.updateReadNews(newsId)
        val action = NewsFragmentDirections.actionNewsToNewsDetail(newsId)
        findNavController().navigate(action)
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
}
