package com.example.simbirsoft_android_practice

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding

class FilterFragment : Fragment(R.layout.fragment_filter) {

    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val filterAdapter by lazy { FilterAdapter() }
    private val filterPrefs by lazy { FilterPreferencesManager(requireContext()) }

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
        val categories = categoriesDto.map { CategoryMapper.toFilter(it, filterPrefs) }
        filterAdapter.submitList(categories)
    }

    private fun setupBackButton() {
        binding.imageViewFilterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupApplySettingsButton() {
        binding.imageViewFilterApplySettings.setOnClickListener {
            val selectedCategories = filterAdapter.currentList.filter { it.isEnabled }.map { it.id }
            filterPrefs.saveSelectedCategories(selectedCategories)
            Toast.makeText(requireContext(), "Фильтры успешно сохранены", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}
