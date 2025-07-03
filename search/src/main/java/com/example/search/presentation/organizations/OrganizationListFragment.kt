package com.example.search.presentation.organizations

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
import com.example.core.model.SearchEvent
import com.example.search.R
import com.example.search.databinding.FragmentSearchListBinding
import com.example.search.di.SearchComponentProvider
import com.example.search.presentation.search.SearchContainerState
import com.example.search.presentation.search.SearchContainerViewModel
import com.example.search.presentation.model.SearchTab
import com.example.search.presentation.adapter.EventAdapter
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrganizationListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val organizationListViewModel by viewModels<OrganizationListViewModel> { viewModelFactory }
    private val searchContainerViewModel: SearchContainerViewModel by viewModels(ownerProducer = { requireParentFragment() }) { viewModelFactory }

    private val adapter: EventAdapter by lazy { EventAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val component = (context.applicationContext as SearchComponentProvider)
            .provideSearchComponent()
        component.injectOrganizationListFragment(this)
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeUiState()
        observeContainerState()
    }

    private fun initRecyclerView() {
        binding.recyclerViewEventItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@OrganizationListFragment.adapter
            ContextCompat.getDrawable(requireContext(), R.drawable.item_search_result_divider)
                ?.let { drawable ->
                    addItemDecoration(
                        DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                            setDrawable(drawable)
                        },
                    )
                }
        }
    }

    private fun observeContainerState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchContainerViewModel.state.collect { state: SearchContainerState ->
                    if (state is SearchContainerState.QueryAndPage && state.tab == SearchTab.ORGANIZATIONS) {
                        organizationListViewModel.onEvent(OrganizationEvent.Load)
                    }
                }
            }
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                organizationListViewModel.uiState.collect { state ->
                    when (state) {
                        is OrganizationListState.Idle -> Unit
                        is OrganizationListState.Success -> showResults(state.organizations)
                    }
                }
            }
        }
    }

    private fun showResults(organizations: List<SearchEvent>) {
        adapter.submitList(organizations)
        binding.apply {
            scrollViewSearchNoQuery.isVisible = false
            recyclerViewEventItem.isVisible = true
            textViewNoResults.isVisible = false
            textViewKeyWords.isVisible = true
            textViewEventCount.isVisible = true
            textViewEventCount.text =
                resources.getQuantityString(
                    R.plurals.search_results_count,
                    organizations.size,
                    organizations.size,
                )
        }
    }

    companion object {
        fun newInstance() = OrganizationListFragment()
    }
}
