package com.example.simbirsoft_android_practice.filter

import android.annotation.SuppressLint
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
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.random.Random

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val binding by viewBinding(FragmentFilterBinding::bind)
    private val filterAdapter by lazy { FilterAdapter() }
    private val filterPrefs by lazy { FilterPreferences(requireContext()) }
    private val categoryRepository by lazy { CategoryRepository(JsonAssetExtractor(requireContext())) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        loadCategoryData()
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

    @SuppressLint("CheckResult")
    private fun loadCategoryData() {
        val categoriesObservable = categoryRepository.getCategories()
            .observeOn(Schedulers.computation())
            .map { categories ->
                categories.map { CategoryMapper.toFilterCategory(it, filterPrefs) }
            }
            .doOnNext { Log.d("RxJava", "Mapped data on thread: ${Thread.currentThread().name}") }
            .observeOn(AndroidSchedulers.mainThread())

        val randomObservable = Observable.fromCallable {
            List(10) { Random.nextInt(100) }
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(Schedulers.io())
            .doOnNext {
                Log.d(
                    "RxJava",
                    "Generated random data on thread: ${Thread.currentThread().name}"
                )
            }

        Observable.zip(categoriesObservable, randomObservable) { categories, _ ->
            Log.d("RxJava", "Combined on thread: ${Thread.currentThread().name}")
            categories
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { filterAdapter.submitList(it) }
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
