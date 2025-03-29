package com.example.simbirsoft_android_practice.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.core.EventRepository
import com.example.simbirsoft_android_practice.core.JsonAssetExtractor
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import dev.androidbroadcast.vbpd.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class EventListFragment : Fragment(R.layout.fragment_search_list) {

    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val adapter = EventAdapter()
    var searchQuery: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        refreshData()
    }

    private fun initRecyclerView() {
        binding.recyclerViewEventItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EventListFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    @SuppressLint("CheckResult")
    fun refreshData() {
        EventRepository(JsonAssetExtractor(requireContext())).getEvents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { fetchedEvents ->
                    val filteredEvents = if (searchQuery.isEmpty()) {
                        binding.linearLayoutSearchNoQuery.visibility = View.VISIBLE
                        binding.textViewEventCount.visibility = View.GONE
                        emptyList()
                    } else {
                        binding.linearLayoutSearchNoQuery.visibility = View.GONE
                        val results = fetchedEvents.filter { event ->
                            event.title.contains(searchQuery, ignoreCase = true)
                        }
                        updateEventCount(results.size)
                        results
                    }
                    adapter.submitList(filteredEvents)
                },
                {
                    binding.linearLayoutSearchNoQuery.visibility = View.VISIBLE
                    binding.textViewEventCount.visibility = View.GONE
                    adapter.submitList(emptyList())
                }
            )
    }

    private fun updateEventCount(count: Int) {
        binding.textViewEventCount.text = requireContext().resources.getQuantityString(
            R.plurals.search_results_count,
            count,
            count
        )
        binding.textViewEventCount.visibility = if (count > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        fun newInstance() = EventListFragment()
    }
}



