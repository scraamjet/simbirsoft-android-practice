package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val categoryAdapter by lazy { CategoryAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadCategoryData()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFilterItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun loadCategoryData() {
        val parser = JsonParser(requireContext())
        val categories = parser.parseCategories("categories.json")
        categoryAdapter.submitList(categories)
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}