package com.example.simbirsoft_android_practice.filter

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.core.JsonParser
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val filterAdapter by lazy { FilterAdapter() }
    private val filterPrefs by lazy { FilterPreferencesManager(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadCategoryData()
    }

    private fun setupUI() {
        setupRecyclerView()
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
        val categoriesDto = JsonParser(requireContext()).parseCategories()
        val categories = categoriesDto.map { category ->
            CategoryMapper.toFilter(category, filterPrefs)
        }
        filterAdapter.submitList(categories)
    }

    private fun setupBackButton() {
        binding.imageViewFilterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupApplySettingsButton() {
        binding.imageViewFilterApplySettings.setOnClickListener {
            saveFilterSettings()
        }
    }

    private fun saveFilterSettings() {
        val selectedCategories = filterAdapter.currentList
            .filter { category -> category.isEnabled }
            .map { category -> category.id }
            .toSet()

        filterPrefs.saveSelectedCategories(selectedCategories)
        Toast.makeText(requireContext(), "Фильтры успешно сохранены", Toast.LENGTH_SHORT).show()
        parentFragmentManager.popBackStack()
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}
