package com.example.simbirsoft_android_practice.presentation.search

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
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import com.example.simbirsoft_android_practice.domain.model.SearchEvent
import dev.androidbroadcast.vbpd.viewBinding
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeState()
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
                            DividerItemDecoration.VERTICAL,
                        ).apply {
                            setDrawable(drawable)
                        },
                    )
                }
        }
    }

    fun refreshData(query: String) {
        viewModel.onEvent(EventListEvent.SearchQueryChanged(query))
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is EventListState.Loading -> showLoading()
                        is EventListState.BlankQuery -> showSearchStub()
                        is EventListState.Empty -> showNoResults()
                        is EventListState.Success -> {
                            showResults(state.results)
                            adapter.submitList(state.results)
                        }
                        is EventListState.Error -> showSearchStub()
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
                events.size,
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

