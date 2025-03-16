package com.example.simbirsoft_android_practice.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.CategoryMapper
import com.example.simbirsoft_android_practice.JsonParser
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import dev.androidbroadcast.vbpd.viewBinding

private const val RECYCLER_VIEW_SPAN_COUNT = 2

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)
    private val jsonParser by lazy { JsonParser(requireContext()) }
    private val adapter by lazy { HelpAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadCategories()
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerViewHelpItem
        recyclerView.layoutManager = GridLayoutManager(requireContext(), RECYCLER_VIEW_SPAN_COUNT)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                RECYCLER_VIEW_SPAN_COUNT,
                resources.getDimensionPixelSize(R.dimen.help_category_item_spacing)
            )
        )
    }

    private fun loadCategories() {
        val categoriesDto = jsonParser.parseCategories()
        val categories = categoriesDto.map { CategoryMapper.toHelpCategory(it) }
        adapter.submitList(categories)
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}