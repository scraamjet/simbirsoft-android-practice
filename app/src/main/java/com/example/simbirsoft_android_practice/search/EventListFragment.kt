package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG_EVENT_LIST_FRAGMENT = "EventFragment"

class EventListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val eventAdapter = EventAdapter()
    private val newsRepository by lazy {
        (requireContext().applicationContext as RepositoryProvider).newsRepository
    }

    private val supervisorJob = SupervisorJob()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(TAG_EVENT_LIST_FRAGMENT, "Coroutine exception", throwable)
        showSearchStub()
        eventAdapter.submitList(emptyList())
    }
    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + supervisorJob + coroutineExceptionHandler)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        coroutineScope.cancel()
    }

    private fun initRecyclerView() {
        binding.recyclerViewEventItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
            val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(requireContext(), R.drawable.item_search_result_divider)
                ?.let { drawable ->
                    divider.setDrawable(drawable)
                }
            addItemDecoration(divider)
        }
    }

    fun refreshData() {
        coroutineScope.launch {
            val newsFlow = if (newsRepository.hasCachedNews()) {
                newsRepository.getNewsFromCacheFlow()
            } else {
                showLoading()
                newsRepository.getNewsWithDelayFlow()
            }

            newsFlow
                .map { list -> list.map(SearchMapper::toEvent) }
                .flowOn(Dispatchers.IO)
                .collect { events ->
                    val searchQuery =
                        (parentFragment as? SearchQueryProvider)?.getSearchQuery().orEmpty()
                    handleFetchedEvents(events, searchQuery)
                }
        }
    }


    private fun handleFetchedEvents(
        fetchedEvents: List<Event>,
        searchQuery: String,
    ) {
        val filteredEvents =
            fetchedEvents.filter { event ->
                event.title.contains(searchQuery, ignoreCase = true)
            }

        when {
            searchQuery.isBlank() -> showSearchStub()
            filteredEvents.isEmpty() -> showNoResults()
            else -> showResults(filteredEvents)
        }

        eventAdapter.submitList(filteredEvents)
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

    private fun showResults(events: List<Event>) {
        binding.apply {
            progressBarSearch.isVisible = false
            scrollViewSearchNoQuery.isVisible = false
            recyclerViewEventItem.isVisible = true
            textViewNoResults.isVisible = false
            textViewKeyWords.isVisible = true
            textViewEventCount.isVisible = true
            textViewEventCount.text =
                resources.getQuantityString(
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
