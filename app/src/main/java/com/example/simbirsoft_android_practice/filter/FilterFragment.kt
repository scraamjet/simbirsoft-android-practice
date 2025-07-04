package com.example.simbirsoft_android_practice.filter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import com.example.simbirsoft_android_practice.launchInLifecycle
import dev.androidbroadcast.vbpd.viewBinding
import javax.inject.Inject

class FilterFragment : Fragment(R.layout.fragment_filter) {
    private val binding by viewBinding(FragmentFilterBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val filterViewModel by viewModels<FilterViewModel> { viewModelFactory }

    private val filterAdapter by lazy { FilterAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        observeCategories()
        observeLoading()
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

    private fun observeCategories() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            filterViewModel.categories.collect { categories ->
                filterAdapter.submitList(categories)
            }
        }
    }

    private fun observeLoading() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            filterViewModel.loading.collect { isLoading ->
                binding.progressBarFilter.isVisible = isLoading
                binding.imageViewFilterApplySettings.isGone = isLoading
                binding.recyclerViewFilterItem.isGone = isLoading
            }
        }
    }

    private fun initClickListeners() {
        binding.imageViewFilterBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imageViewFilterApplySettings.setOnClickListener {
            saveFilterSettings()
        }
    }

    private fun saveFilterSettings() {
        val selectedCategories =
            filterAdapter.currentList.filter { category -> category.isEnabled }
                .map { category -> category.id }.toSet()
        filterViewModel.saveSelected(selectedCategories)
        Toast.makeText(requireContext(), getString(R.string.filter_saved_toast), Toast.LENGTH_SHORT)
            .show()
        findNavController().navigateUp()
    }
}
