package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "EventListFragment"

class EventListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val eventAdapter = EventAdapter()
    private val newsRepository by lazy { NewsRepository(JsonAssetExtractor(requireContext())) }
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    var searchQuery: String = ""

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        refreshData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel()
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
            newsRepository.getNewsFlow()
                .map { newsList ->
                    newsList.map(SearchMapper::toEvent)
                }
                .catch { exception ->
                    Log.e(
                        TAG,
                        "Error while mapping or collecting news: ${exception.localizedMessage}",
                    )
                    showSearchStub()
                    eventAdapter.submitList(emptyList())
                }
                .collect { eventList ->
                    val filteredEvents =
                        eventList.filter { event ->
                            event.title.contains(searchQuery, ignoreCase = true)
                        }

                    when {
                        searchQuery.isBlank() -> showSearchStub()
                        filteredEvents.isEmpty() -> showNoResults()
                        else -> showResults(filteredEvents)
                    }
                    eventAdapter.submitList(filteredEvents)
                }
        }
    }

    private fun showSearchStub() {
        binding.apply {
            recyclerViewEventItem.visibility = View.GONE
            scrollViewSearchNoQuery.visibility = View.VISIBLE
            textViewKeyWords.visibility = View.GONE
            textViewEventCount.visibility = View.GONE
            textViewNoResults.visibility = View.GONE
        }
    }

    private fun showNoResults() {
        binding.apply {
            recyclerViewEventItem.visibility = View.GONE
            scrollViewSearchNoQuery.visibility = View.GONE
            textViewKeyWords.visibility = View.GONE
            textViewEventCount.visibility = View.GONE
            textViewNoResults.visibility = View.VISIBLE
        }
    }

    private fun showResults(events: List<Event>) {
        binding.apply {
            scrollViewSearchNoQuery.visibility = View.GONE
            recyclerViewEventItem.visibility = View.VISIBLE
            textViewNoResults.visibility = View.GONE
            textViewKeyWords.visibility = View.VISIBLE
            textViewEventCount.visibility = View.VISIBLE
            textViewEventCount.text =
                resources.getQuantityString(
                    R.plurals.search_results_count,
                    events.size,
                    events.size,
                )
        }
    }

    companion object {
        fun newInstance() = EventListFragment()
    }
}
