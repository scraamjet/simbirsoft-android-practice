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
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val RECYCLER_VIEW_SPAN_COUNT = 2

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val helpViewModel by viewModels<HelpViewModel> { viewModelFactory }

    private val adapter by lazy { HelpAdapter() }

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
        observeData()
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

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    helpViewModel.loading.collect { isLoading ->
                        binding.progressBarHelp.isVisible = isLoading
                        binding.recyclerViewHelpItem.isVisible = !isLoading
                    }
                }
                launch {
                    helpViewModel.categories.collect { categories ->
                        adapter.submitList(categories)
                    }
                }
            }
        }
    }
}
