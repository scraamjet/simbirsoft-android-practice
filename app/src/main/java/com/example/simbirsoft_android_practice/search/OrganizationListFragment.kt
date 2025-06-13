package com.example.simbirsoft_android_practice.search

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrganizationListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<OrganizationListViewModel> { viewModelFactory }

    private val adapter = EventAdapter()

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
        observeOrganizations()
    }

    private fun initRecyclerView() {
        binding.recyclerViewEventItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@OrganizationListFragment.adapter
            ContextCompat.getDrawable(requireContext(), R.drawable.item_search_result_divider)?.let { drawable ->
                addItemDecoration(
                    DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                        setDrawable(drawable)
                    },
                )
            }
        }
    }

    private fun observeOrganizations() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.organizations.collect { events ->
                    adapter.submitList(events)
                    showResults(events.size)
                }
            }
        }
    }

    fun refreshData() {
        viewModel.refreshOrganizationList()
    }

    private fun showResults(size: Int) {
        binding.apply {
            scrollViewSearchNoQuery.isVisible = false
            recyclerViewEventItem.isVisible = true
            textViewNoResults.isVisible = false
            textViewKeyWords.isVisible = true
            textViewEventCount.isVisible = true
            textViewEventCount.text =
                resources.getQuantityString(
                    R.plurals.search_results_count,
                    size,
                    size,
                )
        }
    }

    companion object {
        fun newInstance() = OrganizationListFragment()
    }
}
