package com.example.simbirsoft_android_practice

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val categoryAdapter by lazy { CategoryAdapter() }
    private val prefs by lazy { requireContext().getSharedPreferences("filter_prefs", Context.MODE_PRIVATE) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadCategoryData()
        setupBackButton()
        setupApplySettingsButton()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFilterItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun loadCategoryData() {
        val parser = JsonParser(requireContext())
        val categories = parser.parseCategories().map { category ->
            category.copy(isEnabled = prefs.getBoolean("category_${category.id}", true))
        }
        categoryAdapter.submitList(categories)
    }

    private fun setupBackButton() {
        binding.imageViewFilterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupApplySettingsButton() {
        binding.imageViewFilterApplySettings.setOnClickListener {
            val selectedCategories = categoryAdapter.currentList.filter { it.isEnabled }.map { it.id }
            prefs.edit().apply {
                categoryAdapter.currentList.forEach { category ->
                    putBoolean("category_${category.id}", category.isEnabled)
                }
                putStringSet("selected_categories", selectedCategories.map { it.toString() }.toSet())
                apply()
            }
            parentFragmentManager.setFragmentResult("filter_result", bundleOf("selectedCategories" to selectedCategories.toIntArray()))
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}