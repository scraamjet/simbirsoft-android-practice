package com.example.simbirsoft_android_practice.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.FilterCategory
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG_FILTER_FRAGMENT = "FilterFragment"
private const val KEY_FILTER_CATEGORIES = "key_filter_categories"

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val filterAdapter by lazy { FilterAdapter() }
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val categoryRepository by lazy {
        RepositoryProvider.fromContext(requireContext()).categoryRepository
    }
    private val compositeDisposable = CompositeDisposable()
    private var filterCategories: List<FilterCategory>? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        restoreState(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveState(outState)
    }

    private fun initRecyclerView() {
        binding.recyclerViewFilterItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filterAdapter
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(requireContext(), R.drawable.item_filter_divider)
                ?.let { drawable ->
                    divider.setDrawable(drawable)
                }
            addItemDecoration(divider)
        }
    }

    private fun loadCategoryData() {
        showLoading()

        viewLifecycleOwner.lifecycleScope.launch {
            categoryRepository.getCategories()
                .flowOn(Dispatchers.IO)
                .map { list ->
                    list.map { CategoryMapper.toFilterCategory(it, filterPrefs) }
                }
                .catch { throwable ->
                    Log.w(
                        TAG_FILTER_FRAGMENT,
                        "Flow exception: ${throwable.localizedMessage}",
                        throwable,
                    )
                }
                .collect { categories ->
                    showData(categories)
                }
        }
    }

    private fun initClickListeners() {
        binding.imageViewFilterBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.imageViewFilterApplySettings.setOnClickListener {
            saveFilterSettings()
        }
    }

    private fun saveFilterSettings() {
        val selectedCategories =
            filterAdapter.currentList
                .filter { category -> category.isEnabled }
                .map { category -> category.id }
                .toSet()
        filterPrefs.saveSelectedCategories(selectedCategories)
        Toast.makeText(requireContext(), getString(R.string.filter_saved_toast), Toast.LENGTH_SHORT)
            .show()
        parentFragmentManager.popBackStack()
    }

    private fun showLoading() {
        binding.progressBarFilter.isVisible = true
        binding.imageViewFilterApplySettings.isVisible = false
        binding.recyclerViewFilterItem.isVisible = false
    }

    private fun showData(categories: List<FilterCategory>) {
        binding.progressBarFilter.isVisible = false
        binding.imageViewFilterApplySettings.isVisible = true
        binding.recyclerViewFilterItem.isVisible = true
        filterCategories = categories
        filterAdapter.submitList(categories)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState?.containsKey(KEY_FILTER_CATEGORIES) == true) {
            val saved =
                BundleCompat.getParcelableArrayList(
                    savedInstanceState,
                    KEY_FILTER_CATEGORIES,
                    FilterCategory::class.java,
                )
            saved?.let { restoredCategories ->
                filterCategories = restoredCategories
                showData(restoredCategories)
            }
        } else {
            loadCategoryData()
        }
    }

    private fun saveState(outState: Bundle) {
        val categories = filterCategories?.let(::ArrayList) ?: return
        outState.putParcelableArrayList(KEY_FILTER_CATEGORIES, categories)
    }

    companion object {
        fun newInstance() = FilterFragment()
    }
}
