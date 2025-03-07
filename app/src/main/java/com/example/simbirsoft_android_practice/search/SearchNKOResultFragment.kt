package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchResultsBinding
import dev.androidbroadcast.vbpd.viewBinding

private const val NKO_LIST_SIZE = 5

class SearchNKOResultFragment : Fragment(R.layout.fragment_search_results) {
    private val binding by viewBinding(FragmentSearchResultsBinding::bind)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val events =
            List(NKO_LIST_SIZE) {
                Event(generateRandomString())
            }
        binding.searchNkoItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = EventAdapter(events)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    companion object {
        fun newInstance() = SearchNKOResultFragment()
    }
}
