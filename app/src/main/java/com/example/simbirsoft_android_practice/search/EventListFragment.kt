package com.example.simbirsoft_android_practice.search

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import com.example.simbirsoft_android_practice.model.SearchEvent
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class EventListFragment : Fragment(R.layout.fragment_search_list) {

    private val binding by viewBinding(FragmentSearchListBinding::bind)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<EventListViewModel> { viewModelFactory }

    private val adapter = EventAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerViewEventItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EventListFragment.adapter
            ContextCompat.getDrawable(requireContext(), R.drawable.item_search_result_divider)
                ?.let { drawable ->
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        ).apply {
                            setDrawable(drawable)
                        }
                    )
                }
        }
    }

    fun refreshData(debouncedFlow: Flow<String>) {
        viewModel.observeSearchQuery(debouncedFlow)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        when (state) {
                            UiState.Loading -> showLoading()
                            UiState.BlankQuery, UiState.Error -> showSearchStub() // объединили
                            UiState.Empty -> showNoResults()
                            UiState.Success -> showResults(viewModel.filteredEvents.value)
                        }
                    }
                }
                launch {
                    viewModel.filteredEvents.collect { events ->
                        adapter.submitList(events)
                    }
                }
            }
        }
    }

    private fun showSearchStub() {
        binding.apply {
            progressBarSearch.isVisible = false
            recyclerViewEventItem.isVisible = false
            scrollViewSearchNoQuery.isVisible = true
            textViewKeyWords.isVisible = false
            textViewEventCount.isVisible = false
            textViewNoResults.isVisible = false
        }
    }

    private fun showNoResults() {
        binding.apply {
            progressBarSearch.isVisible = false
            recyclerViewEventItem.isVisible = false
            scrollViewSearchNoQuery.isVisible = false
            textViewKeyWords.isVisible = false
            textViewEventCount.isVisible = false
            textViewNoResults.isVisible = true
        }
    }

    private fun showResults(events: List<SearchEvent>) {
        binding.apply {
            progressBarSearch.isVisible = false
            scrollViewSearchNoQuery.isVisible = false
            recyclerViewEventItem.isVisible = true
            textViewNoResults.isVisible = false
            textViewKeyWords.isVisible = true
            textViewEventCount.isVisible = true
            textViewEventCount.text = resources.getQuantityString(
                R.plurals.search_results_count,
                events.size,
                events.size
            )
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBarSearch.isVisible = true
            recyclerViewEventItem.isVisible = false
            scrollViewSearchNoQuery.isVisible = false
            textViewKeyWords.isVisible = false
            textViewEventCount.isVisible = false
            textViewNoResults.isVisible = false
        }
    }

    companion object {
        fun newInstance() = EventListFragment()
    }
}

