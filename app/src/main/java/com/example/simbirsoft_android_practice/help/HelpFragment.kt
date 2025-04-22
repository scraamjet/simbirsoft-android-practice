package com.example.simbirsoft_android_practice.help

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.BundleCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.HelpCategory
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val RECYCLER_VIEW_SPAN_COUNT = 2
private const val KEY_HELP_CATEGORIES = "key_help_categories"
private const val TAG_HELP_FRAGMENT = "HelpFragment"

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)
    private val categoryRepository by lazy {
        RepositoryProvider.fromContext(requireContext()).categoryRepository
    }
    private val adapter by lazy { HelpAdapter() }
    private var categoriesItems: List<HelpCategory>? = null
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
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

    private fun loadCategoryData() {
        val disposable =
            if (categoryRepository.hasCachedCategories()) {
                categoryRepository.getCategoriesFromCache()
            } else {
                showLoading()
                categoryRepository.getCategoriesFromStorage()
            }
                .doOnSubscribe {
                    Log.d(TAG_HELP_FRAGMENT, "Subscribed to categories on thread: ${Thread.currentThread().name}")
                }
                .subscribeOn(Schedulers.io())
                .map { list -> list.map(CategoryMapper::toHelpCategory) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { categories ->
                    Log.d(
                        TAG_HELP_FRAGMENT,
                        "Received help categories on thread: ${Thread.currentThread().name}, count: ${categories.size}",
                    )
                }
                .subscribe { categories -> showData(categories) }

        compositeDisposable.add(disposable)
    }

    private fun showLoading() {
        binding.progressBarHelp.isVisible = true
        binding.recyclerViewHelpItem.isVisible = false
    }

    private fun showData(helpCategories: List<HelpCategory>?) {
        binding.apply {
            progressBarHelp.isVisible = false
            recyclerViewHelpItem.isVisible = true
        }
        categoriesItems = helpCategories
        adapter.submitList(helpCategories)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState?.containsKey(KEY_HELP_CATEGORIES) == true) {
            savedInstanceState.let { bundle ->
                val savedCategories =
                    BundleCompat.getParcelableArrayList(
                        bundle,
                        KEY_HELP_CATEGORIES,
                        HelpCategory::class.java,
                    )
                savedCategories?.let { restoredCategories ->
                    categoriesItems = restoredCategories
                    showData(restoredCategories)
                }
            }
        } else {
            loadCategoryData()
        }
    }

    private fun saveState(outState: Bundle) {
        val categories = categoriesItems?.let(::ArrayList) ?: return
        outState.putParcelableArrayList(KEY_HELP_CATEGORIES, categories)
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}
