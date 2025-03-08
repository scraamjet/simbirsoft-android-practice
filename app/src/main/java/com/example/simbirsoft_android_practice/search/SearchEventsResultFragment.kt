package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchResultsBinding
import com.example.simbirsoft_android_practice.utils.generateRandomString
import dev.androidbroadcast.vbpd.viewBinding

private const val EVENTS_LIST_SIZE = 5

class SearchEventsResultFragment : Fragment(R.layout.fragment_search_results) {
    private val binding by viewBinding(FragmentSearchResultsBinding::bind)
    private val adapter = EventAdapter(generateEventsList())

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.searchNkoItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SearchEventsResultFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    fun refreshData() {
        adapter.updateEvents(generateEventsList())
    }

    private fun generateEventsList(): List<Event> {
        return List(EVENTS_LIST_SIZE) { Event(generateRandomString()) }
    }

    companion object {
        fun newInstance() = SearchEventsResultFragment()
    }
}
