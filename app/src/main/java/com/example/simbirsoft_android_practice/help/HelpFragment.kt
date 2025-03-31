package com.example.simbirsoft_android_practice.help

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.CategoryRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import com.example.simbirsoft_android_practice.filter.CategoryMapper
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

private const val RECYCLER_VIEW_SPAN_COUNT = 2

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)
    private val categoryRepository by lazy { CategoryRepository(JsonAssetExtractor(requireContext())) }
    private val adapter by lazy { HelpAdapter() }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
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
                resources.getDimensionPixelSize(R.dimen.help_category_item_spacing),
            ),
        )
    }

    @SuppressLint("CheckResult")
    private fun loadCategories() {
        categoryRepository.getCategories()
            .observeOn(Schedulers.computation())
            .map { categories -> categories.map(CategoryMapper::toHelpCategory) }
            .doOnNext { Log.d("RxJava", "Processed help categories on: ${Thread.currentThread().name}") }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { adapter.submitList(it) }
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}
