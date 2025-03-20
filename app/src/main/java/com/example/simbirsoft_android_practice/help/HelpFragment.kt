package com.example.simbirsoft_android_practice.help

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
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
private const val TIMEOUT = 5000L


class HelpFragment : Fragment(R.layout.fragment_help) {

    private val binding by viewBinding(FragmentHelpBinding::bind)
    private var jsonParser: JsonParser? = null
    private val adapter by lazy { HelpAdapter() }
    private val executor = Executors.newSingleThreadExecutor()
    private var categories: List<HelpCategory>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        jsonParser = JsonParser(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        if (savedInstanceState != null) {
            categories = BundleCompat.getParcelableArrayList(
                savedInstanceState,
                KEY_CATEGORIES,
                HelpCategory::class.java
            )
            categories?.let { adapter.submitList(it) }
        } else {
            loadCategories()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        categories?.let { outState.putParcelableArrayList(KEY_CATEGORIES, ArrayList(it)) }
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
        binding.progressBarHelp.isVisible = true
        executor.execute {
            Thread.sleep(TIMEOUT)
            val parsedCategories = jsonParser?.parseCategories()
            val helpCategories = parsedCategories?.map(CategoryMapper::toHelpCategory)
            Handler(Looper.getMainLooper()).post {
                if (!isAdded) {
                    return@post
                }
                categories = helpCategories
                adapter.submitList(helpCategories)
                binding.progressBarHelp.isVisible = false
            }
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

