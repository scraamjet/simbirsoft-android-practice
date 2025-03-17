package com.example.simbirsoft_android_practice.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.core.JsonParser
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.filter.FilterFragment
import com.example.simbirsoft_android_practice.filter.FilterPreferencesManager
import dev.androidbroadcast.vbpd.viewBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val filterPrefs by lazy { FilterPreferencesManager(requireContext()) }
    private val newsPrefs by lazy { NewsPreferencesManager(requireContext()) }
    private val newsAdapter by lazy { NewsAdapter(::onNewsItemSelected) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadNewsData()
    }

    private fun setupUI() {
        setupRecyclerView()
        setupFilterButton()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
    }

    private fun loadNewsData() {
        val allNews = JsonParser(requireContext()).parseNews()
        val selectedCategoryIds = filterPrefs.getSelectedCategories()

        val filteredNewsItems = allNews
            .filter { news ->
                news.listHelpCategoryId.any { categoryId ->
                    selectedCategoryIds.contains(
                        categoryId
                    )
                }
            }
            .map(NewsMapper::toNewsItem)

        newsAdapter.submitList(filteredNewsItems)
    }

    private fun setupFilterButton() {
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
        loadNewsData()
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}
