package com.example.simbirsoft_android_practice

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import dev.androidbroadcast.vbpd.viewBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferencesManager(requireContext()) }
    private val newsPrefs by lazy { NewsPreferencesManager(requireContext()) }

    private val newsAdapter by lazy {
        NewsAdapter { newsId ->
            newsPrefs.saveSelectedNewsId(newsId)
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadNewsData()
        setupFilterButton()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun loadNewsData() {
        val parser = JsonParser(requireContext())
        val allNews = parser.parseNews()
        val selectedCategoryIds = filterPrefs.getSelectedCategories()
        val filteredNews = selectedCategoryIds.let { ids ->
            allNews.filter { news -> news.listHelpCategoryId.any { it in ids } }
        } ?: allNews

        newsAdapter.submitList(filteredNews.map { NewsMapper.toNewsItem(it) })
    }

    private fun setupFilterButton() {
        binding.imageViewButtonFilters.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, FilterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        loadNewsData()
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
