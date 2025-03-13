package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
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
        setupBackButton()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFilterItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
            val divider = ContextCompat.getDrawable(context, R.drawable.item_search_result_divider)
            val dividerItemDecoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)

            divider?.let {
                dividerItemDecoration.setDrawable(it)
            }

            addItemDecoration(dividerItemDecoration)
        }
    }

    private fun loadCategoryData() {
        val parser = JsonParser(requireContext())
        val categories = parser.parseCategories()
        categories.forEach { it.isEnabled = true }
        categoryAdapter.submitList(categories)
    }

    private fun setupBackButton() {
        binding.imageViewFilterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}