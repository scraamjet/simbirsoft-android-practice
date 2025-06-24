package com.example.simbirsoft_android_practice.filter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentFilterBinding
import com.example.simbirsoft_android_practice.domain.model.FilterCategory
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initClickListeners()
        observeUiState()
        observeEffects()
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

    private fun initClickListeners() {
        binding.imageViewFilterBack.setOnClickListener {
            filterViewModel.onEvent(FilterEvent.OnBackClicked)
        }
        binding.imageViewFilterApplySettings.setOnClickListener {
            val selectedCategories = filterAdapter.currentList
                .filter { category -> category.isEnabled }
                .map { category -> category.id }
                .toSet()
            filterViewModel.onEvent(FilterEvent.OnApplyClicked(selectedCategories))
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.state.collect { filterState ->
                    when (filterState) {
                        is FilterState.Loading -> showLoading()
                        is FilterState.Success -> showResult(filterState.categories)
                        is FilterState.Error -> showError()
                    }
                }
            }
        }
    }

    private fun observeEffects() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filterViewModel.effect.collect { filterEffect ->
                    when (filterEffect) {
                        is FilterEffect.NavigateBack -> findNavController().navigateUp()
                        is FilterEffect.ShowToast -> Toast.makeText(
                            requireContext(),
                            getString(filterEffect.messageResId),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBarFilter.isVisible = true
        binding.imageViewFilterApplySettings.isVisible = false
        binding.recyclerViewFilterItem.isVisible = false
    }

    private fun showResult(categoryList: List<FilterCategory>) {
        binding.progressBarFilter.isVisible = false
        binding.imageViewFilterApplySettings.isVisible = true
        binding.recyclerViewFilterItem.isVisible = true
        filterAdapter.submitList(categoryList)
    }

    private fun showError() {
        binding.progressBarFilter.isVisible = false
        binding.imageViewFilterApplySettings.isVisible = false
        binding.recyclerViewFilterItem.isVisible = false
    }
}


