package com.example.simbirsoft_android_practice

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.google.gson.Gson
import dev.androidbroadcast.vbpd.viewBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val newsAdapter by lazy { NewsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadNewsData()
        setupFilterButton()
        setupFilterResultListener()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewItemNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = newsAdapter
        }
        newsAdapter.setOnItemClickListener { news ->
            saveNewsToPreferences(news)
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadNewsData(selectedCategoryIds: List<Int>? = null) {
        val parser = JsonParser(requireContext())
        val allNews = parser.parseNews()
        val filteredNews = selectedCategoryIds?.let { ids ->
            allNews.filter { news -> news.listHelpCategoryId.any { it in ids } }
        } ?: allNews
        newsAdapter.submitList(filteredNews)
    }

    private fun setupFilterButton() {
        binding.imageViewButtonFilters.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, FilterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupFilterResultListener() {
        parentFragmentManager.setFragmentResultListener("filter_result", viewLifecycleOwner) { _, bundle ->
            val selectedCategories = bundle.getIntArray("selectedCategories")?.toList()
            loadNewsData(selectedCategories)
        }
    }

    private fun saveNewsToPreferences(news: News) {
        val sharedPreferences = requireContext().getSharedPreferences("news_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("selected_news", Gson().toJson(news))
            apply()
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}