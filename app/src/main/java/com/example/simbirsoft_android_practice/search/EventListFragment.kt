package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.NewsRepository
import com.example.simbirsoft_android_practice.core.RepositoryProvider
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class EventListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val eventAdapter = EventAdapter()
    private val newsRepository: NewsRepository
        get() = (requireActivity().application as RepositoryProvider).newsRepository
    private val compositeDisposable = CompositeDisposable()
    var searchQuery: String = ""

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
        val disposable = if (newsRepository.getCachedNews() == null) {
            showLoading()
            newsRepository.getNewsWithDelay()
        } else {
            newsRepository.getNewsFromCache()
        }
            .subscribeOn(Schedulers.io())
            .map { newsList -> newsList.map(SearchMapper::toEvent) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { fetchedEvents ->
                    handleFetchedEvents(fetchedEvents)
                },
                {
                    showSearchStub()
                    eventAdapter.submitList(emptyList())
                }
            )

        compositeDisposable.add(disposable)
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

    private fun handleFetchedEvents(fetchedEvents: List<Event>) {
        val filteredEvents = fetchedEvents.filter { event ->
            event.title.contains(searchQuery, ignoreCase = true)
        }

        when {
            searchQuery.isBlank() -> showSearchStub()
            filteredEvents.isEmpty() -> showNoResults()
            else -> showResults(filteredEvents)
        }

        eventAdapter.submitList(filteredEvents)
    }


    companion object {
        fun newInstance() = EventListFragment()
    }
}
