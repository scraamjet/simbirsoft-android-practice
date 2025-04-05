package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchListBinding
import com.example.simbirsoft_android_practice.utils.generateRandomString
import dev.androidbroadcast.vbpd.viewBinding

private const val ORGANIZATIONS_LIST_SIZE = 5

class OrganizationListFragment : Fragment(R.layout.fragment_search_list) {
    private val binding by viewBinding(FragmentSearchListBinding::bind)
    private val adapter = EventAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        refreshData()
    }

    private fun initRecyclerView() {
        binding.recyclerViewEventItem.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@OrganizationListFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    fun refreshData() {
        adapter.submitList(generateEventsList())
    }

    private fun generateEventsList(): List<Event> {
        return List(ORGANIZATIONS_LIST_SIZE) {
            Event(
                kotlin.random.Random.nextInt(1, 100),
                generateRandomString()
            )
        }
    }

    companion object {
        fun newInstance() = OrganizationListFragment()
    }
}
