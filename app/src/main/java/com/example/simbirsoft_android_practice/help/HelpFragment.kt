package com.example.simbirsoft_android_practice.help

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import com.example.simbirsoft_android_practice.domain.model.HelpCategory
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RECYCLER_VIEW_SPAN_COUNT = 2

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val helpViewModel by viewModels<HelpViewModel> { viewModelFactory }
    private val helpAdapter by lazy { HelpAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeUiState()
    }

    private fun initRecyclerView() {
        binding.recyclerViewHelpItem.apply {
            layoutManager = GridLayoutManager(requireContext(), RECYCLER_VIEW_SPAN_COUNT)
            adapter = helpAdapter
            addItemDecoration(
                GridSpacingItemDecoration(
                    RECYCLER_VIEW_SPAN_COUNT,
                    resources.getDimensionPixelSize(R.dimen.help_category_item_spacing),
                ),
            )
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                helpViewModel.state.collect { helpState ->
                    when (helpState) {
                        is HelpState.Loading -> showLoading()
                        is HelpState.Success -> showResult(helpState.categories)
                        is HelpState.Error -> showError()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBarHelp.isVisible = true
        binding.recyclerViewHelpItem.isVisible = false
    }

    private fun showResult(categoryList: List<HelpCategory>) {
        binding.progressBarHelp.isVisible = false
        binding.recyclerViewHelpItem.isVisible = true
        helpAdapter.submitList(categoryList)
    }

    private fun showError() {
        binding.progressBarHelp.isVisible = false
        binding.recyclerViewHelpItem.isVisible = false
    }
}
