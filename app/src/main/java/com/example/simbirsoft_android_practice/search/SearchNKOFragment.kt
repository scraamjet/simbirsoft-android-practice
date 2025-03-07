package com.example.simbirsoft_android_practice.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.data.Event
import com.example.simbirsoft_android_practice.databinding.FragmentSearchByNkoBinding
import dev.androidbroadcast.vbpd.viewBinding

class SearchNKOFragment : Fragment(R.layout.fragment_search_by_nko) {
    private val binding by viewBinding(FragmentSearchByNkoBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.searchNkoItemRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = EventAdapter(generateEventsList())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun generateEventsList(): List<Event> {
        return listOf(
            Event("Благотворительный фонд Алины Кабаевой", R.drawable.icon_chevron_right),
            Event("«Во имя жизни»", R.drawable.icon_chevron_right),
            Event("Благотворительный фонд В. Потанина", R.drawable.icon_chevron_right),
            Event("«Детские домики»", R.drawable.icon_chevron_right),
            Event("«Мозайка счастья»", R.drawable.icon_chevron_right)
        )
    }

    companion object {
        fun newInstance() = SearchNKOFragment()
    }
}