package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.SearchEvent
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import com.example.simbirsoft_android_practice.utils.generateRandomString
import dev.androidbroadcast.vbpd.viewBinding
import kotlin.random.Random

private const val ORGANIZATIONS_LIST_SIZE = 5
private const val EVENT_ID_MIN = 1
private const val EVENT_ID_MAX = 100

class OrganizationListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val eventAdapter = EventAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        refreshData()
        showResults()
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
        eventAdapter.submitList(generateEventsList())
    }

    private fun generateEventsList(): List<SearchEvent> {
        return List(ORGANIZATIONS_LIST_SIZE) {
            SearchEvent(
                Random.nextInt(EVENT_ID_MIN, EVENT_ID_MAX),
                generateRandomString(),
            )
        }
    }

    private fun showResults() {
        binding.apply {
            scrollViewSearchNoQuery.visibility = View.GONE
            recyclerViewEventItem.visibility = View.VISIBLE
            textViewNoResults.visibility = View.GONE
            textViewKeyWords.visibility = View.VISIBLE
            textViewEventCount.visibility = View.VISIBLE
            textViewEventCount.text =
                resources.getQuantityString(
                    R.plurals.search_results_count,
                    ORGANIZATIONS_LIST_SIZE,
                    ORGANIZATIONS_LIST_SIZE,
                )
        }
    }

    companion object {
        fun newInstance() = OrganizationListFragment()
    }
}
