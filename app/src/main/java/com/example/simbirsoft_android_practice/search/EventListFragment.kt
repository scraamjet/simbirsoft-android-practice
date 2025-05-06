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
import com.example.simbirsoft_android_practice.data.SearchEvent
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

private const val TAG_EVENT_LIST_FRAGMENT = "EventFragment"

class EventListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val eventAdapter = EventAdapter()
    private val eventRepository by lazy {
        RepositoryProvider.fromContext(requireContext()).eventRepository
    }
    private val compositeDisposable = CompositeDisposable()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
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
        showLoading()

        val disposable =
            eventRepository.getEventsObservable(null)
                .doOnSubscribe {
                    Log.d(
                        TAG_EVENT_LIST_FRAGMENT,
                        "Subscribed to news on thread: ${Thread.currentThread().name}",
                    )
                }
                .subscribeOn(Schedulers.io())
                .map { newsList -> newsList.map(SearchMapper::toSearchEvent) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { events ->
                    Log.d(
                        TAG_EVENT_LIST_FRAGMENT,
                        "Received events on thread: ${Thread.currentThread().name}, count: ${events.size}",
                    )
                }
                .subscribe(
                    { fetchedEvents ->
                        val searchQuery =
                            (parentFragment as? SearchQueryProvider)?.getSearchQuery().orEmpty()
                        handleFetchedEvents(fetchedEvents, searchQuery)
                    },
                    {
                        showSearchStub()
                        eventAdapter.submitList(emptyList())
                    },
                )

        compositeDisposable.add(disposable)
    }

    private fun handleFetchedEvents(
        fetchedEvents: List<SearchEvent>,
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

    private fun showResults(searchEvents: List<SearchEvent>) {
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
                    searchEvents.size,
                    searchEvents.size,
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
