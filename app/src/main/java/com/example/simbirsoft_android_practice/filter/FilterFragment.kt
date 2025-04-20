package com.example.simbirsoft_android_practice.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.FilterCategory
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG_FILTER_FRAGMENT = "FilterFragment"
private const val KEY_FILTER_CATEGORIES = "key_filter_categories"

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val filterAdapter by lazy { FilterAdapter() }
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val categoryRepository by lazy {
        (requireContext().applicationContext as RepositoryProvider).categoryRepository
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
        val disposable =
            if (categoryRepository.hasCachedCategories()) {
                categoryRepository.getCategoriesFromCache()
            } else {
                showLoading()
                categoryRepository.getCategoriesWithDelay()
            }
                .doOnSubscribe {
                    Log.d(
                        TAG_FILTER_FRAGMENT,
                        "Subscribed to categories on thread: ${Thread.currentThread().name}",
                    )
                }
                .subscribeOn(Schedulers.io())
                .map { list ->
                    list.map { categories -> CategoryMapper.toFilterCategory(categories, filterPrefs) }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { categories ->
                    Log.d(
                        TAG_FILTER_FRAGMENT,
                        "Received categories on thread: ${Thread.currentThread().name}, count: ${categories.size}",
                    )
                }
                .subscribe { categories -> showData(categories) }

        compositeDisposable.add(disposable)
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
        binding.progressBarHelp.isVisible = true
        binding.imageViewFilterApplySettings.isVisible = false
        binding.recyclerViewFilterItem.isVisible = false
    }

    private fun showData(categories: List<FilterCategory>) {
        binding.progressBarHelp.isVisible = false
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
