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
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.data.HelpCategory
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import dev.androidbroadcast.vbpd.viewBinding
import java.util.concurrent.Executors

private const val RECYCLER_VIEW_SPAN_COUNT = 2
private const val KEY_CATEGORIES = "key_categories"
private const val TIMEOUT_IN_MILLIS = 5_000L

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)
    private var categoryRepository: CategoryRepository? = null
    private val adapter by lazy { HelpAdapter() }
    private val executor = Executors.newSingleThreadExecutor()
    private var categories: List<HelpCategory>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        categoryRepository = CategoryRepository(JsonAssetExtractor(context))
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        executor.shutdown()
    }

    private fun initRecyclerView() {
        binding.recyclerViewHelpItem.apply {
            layoutManager = GridLayoutManager(requireContext(), RECYCLER_VIEW_SPAN_COUNT)
            adapter = this@HelpFragment.adapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    RECYCLER_VIEW_SPAN_COUNT,
                    resources.getDimensionPixelSize(R.dimen.help_category_item_spacing),
                ),
            )
        }
    }

    private fun loadCategories() {
        showLoading()
        executor.execute {
            Thread.sleep(TIMEOUT_IN_MILLIS)
            val parsedCategories = categoryRepository?.getCategories()
            val helpCategories = parsedCategories?.map(CategoryMapper::toHelpCategory)

            Handler(Looper.getMainLooper()).post {
                if (isAdded) {
                    showData(helpCategories)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBarHelp.isVisible = true
        binding.recyclerViewHelpItem.isVisible = false
    }

    private fun showData(helpCategories: List<HelpCategory>?) {
        categories = helpCategories
        adapter.submitList(helpCategories)
        binding.apply {
            progressBarHelp.isVisible = false
            recyclerViewHelpItem.isVisible = true
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loadCategories()
        } else {
            val savedCategories =
                BundleCompat.getParcelableArrayList(
                    savedInstanceState,
                    KEY_CATEGORIES,
                    HelpCategory::class.java,
                )
            savedCategories?.let { restoredCategories ->
                categories = restoredCategories
                adapter.submitList(restoredCategories)
            }
        }
    }

    private fun saveState(outState: Bundle) {
        categories?.let { savedCategoriesList ->
            outState.putParcelableArrayList(KEY_CATEGORIES, ArrayList(savedCategoriesList))
        }
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}
