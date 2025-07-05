package com.example.simbirsoft_android_practice.presentation.help

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.domain.model.HelpCategory
import com.example.simbirsoft_android_practice.launchInLifecycle
import dev.androidbroadcast.vbpd.viewBinding
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeState()
        observeEffect()
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

    private fun observeEffect() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            helpViewModel.effect.collect { effect ->
                when (effect) {
                    is HelpEffect.ShowErrorToast -> showToast(effect.messageResId)
                }
            }
        }
    }

    private fun observeState() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            helpViewModel.state.collect { state ->
                when (state) {
                    is HelpState.Loading -> showLoading()
                    is HelpState.Result -> showResult(state.categories)
                    is HelpState.Error -> hideContentOnError()
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

    private fun showToast(
        @StringRes messageResId: Int,
    ) {
        Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
    }

    private fun hideContentOnError() {
        binding.progressBarHelp.isVisible = false
        binding.recyclerViewHelpItem.isVisible = false
    }
}
