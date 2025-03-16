package com.example.simbirsoft_android_practice

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import com.example.simbirsoft_android_practice.main.MainActivity
import com.google.gson.Gson
import dev.androidbroadcast.vbpd.viewBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val newsAdapter by lazy { NewsAdapter() }
    private val prefs by lazy {
        requireContext().getSharedPreferences("filter_prefs", Context.MODE_PRIVATE)
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

        newsAdapter.setOnItemClickListener { newsItem ->
            saveSelectedNewsId(newsItem.id)
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, NewsDetailFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadNewsData() {
        val parser = JsonParser(requireContext())
        val allNews = parser.parseNews()
        val selectedCategoryIds = getSavedCategories()
        val filteredNews = selectedCategoryIds?.let { ids ->
            allNews.filter { news -> news.listHelpCategoryId.any { it in ids } }
        } ?: allNews

        newsAdapter.submitList(filteredNews.map { it.toNewsItem() })
    }

    private fun getSavedCategories(): List<Int>? {
        val categoriesSet = prefs.getStringSet("selected_categories", null)
        return categoriesSet?.map { it.toInt() }
    }

    private fun setupFilterButton() {
        binding.imageViewButtonFilters.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, FilterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun saveSelectedNewsId(newsId: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("news_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("selected_news_id", newsId)
            apply()
        }
    }

    override fun onResume() {
        super.onResume()
        loadNewsData() // Обновляем список новостей при возвращении к фрагменту
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}