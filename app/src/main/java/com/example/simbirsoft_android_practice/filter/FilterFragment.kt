package com.example.simbirsoft_android_practice.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG_FILTER_FRAGMENT = "FilterFragment"
private const val TAG_RANDOM_STRING = "RandomString"

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val filterAdapter by lazy { FilterAdapter() }
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val categoryRepository by lazy { CategoryRepository(JsonAssetExtractor(requireContext())) }
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        loadCategoryData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
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
            categoryRepository.getCombinedCategories()
                .doOnSubscribe {
                    Log.d(
                        TAG_FILTER_FRAGMENT,
                        "Subscribed on thread: ${Thread.currentThread().name}",
                    )
                }
                .subscribeOn(Schedulers.io())
                .doOnNext { (_, randomString) ->
                    Log.d(TAG_RANDOM_STRING, "Generated random string: $randomString")
                }
                .map { (categories, _) ->
                    categories.map { category ->
                        CategoryMapper.toFilterCategory(
                            category,
                            filterPrefs,
                        )
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    Log.d(
                        TAG_FILTER_FRAGMENT,
                        "Mapped categories on thread: ${Thread.currentThread().name}",
                    )
                }
                .subscribe { mappedCategories ->
                    filterAdapter.submitList(mappedCategories)
                }

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

    companion object {
        fun newInstance() = FilterFragment()
    }
}
