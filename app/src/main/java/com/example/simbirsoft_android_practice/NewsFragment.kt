package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentNewsBinding
import dev.androidbroadcast.vbpd.viewBinding

class NewsFragment : Fragment(R.layout.fragment_news) {

    private val binding by viewBinding(FragmentNewsBinding::bind)
    private val newsAdapter by lazy { NewsAdapter() }

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
        val newsList = parser.parseNews()
        newsAdapter.submitList(newsList)
    }

    private fun setupFilterButton() {
        binding.imageViewButtonFilters.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, FilterFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun newInstance() = NewsFragment()
    }
}