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
    private val filterAdapter by lazy { FilterAdapter() }
    private val prefs by lazy {
        requireContext().getSharedPreferences(
            "filter_prefs",
            Context.MODE_PRIVATE
        )
    }

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
            adapter = filterAdapter
        }
    }

    private fun loadCategoryData() {
        val parser = JsonParser(requireContext())
        val categoriesDto = parser.parseCategories()
        val categories = categoriesDto.map { it.toFilter(prefs) }
        filterAdapter.submitList(categories)
    }

    private fun setupBackButton() {
        binding.imageViewFilterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupApplySettingsButton() {
        binding.imageViewFilterApplySettings.setOnClickListener {
            val selectedCategories =
                filterAdapter.currentList.filter { it.isEnabled }.map { it.id }
            prefs.edit().apply {
                filterAdapter.currentList.forEach { category ->
                    putBoolean("category_${category.id}", category.isEnabled)
                }
                putStringSet(
                    "selected_categories",
                    selectedCategories.map { it.toString() }.toSet()
                )
                apply()
            }
            parentFragmentManager.setFragmentResult(
                "filter_result",
                bundleOf("selectedCategories" to selectedCategories.toIntArray())
            )
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}