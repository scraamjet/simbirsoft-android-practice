package com.example.simbirsoft_android_practice.help

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.JsonParser
import com.example.simbirsoft_android_practice.data.HelpCategory
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import com.example.simbirsoft_android_practice.filter.CategoryMapper

import dev.androidbroadcast.vbpd.viewBinding
import java.util.concurrent.Executors

private const val RECYCLER_VIEW_SPAN_COUNT = 2
private const val KEY_CATEGORIES = "key_categories"

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)
    private val jsonParser by lazy { JsonParser(requireContext()) }
    private val adapter by lazy { HelpAdapter() }
    private val executor = Executors.newSingleThreadExecutor()
    private var categories: List<HelpCategory>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        if (savedInstanceState != null) {
            categories = savedInstanceState.getParcelableArrayList(KEY_CATEGORIES)
            categories?.let { adapter.submitList(it) }
        } else {
            loadCategories()
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerViewHelpItem
        recyclerView.layoutManager = GridLayoutManager(requireContext(), RECYCLER_VIEW_SPAN_COUNT)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                RECYCLER_VIEW_SPAN_COUNT,
                resources.getDimensionPixelSize(R.dimen.help_category_item_spacing),
            ),
        )
    }

    private fun loadCategories() {
        binding.progressBarHelp.visibility = View.VISIBLE

        executor.execute {
            Thread.sleep(5000)

            val parsedCategories = jsonParser.parseCategories()
            val helpCategories = parsedCategories.map(CategoryMapper::toHelpCategory)

            Handler(Looper.getMainLooper()).post {
                categories = helpCategories
                adapter.submitList(helpCategories)
                binding.progressBarHelp.visibility = View.GONE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        categories?.let { categoryList ->
            outState.putParcelableArrayList(KEY_CATEGORIES, ArrayList(categoryList))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        executor.shutdown()
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}
