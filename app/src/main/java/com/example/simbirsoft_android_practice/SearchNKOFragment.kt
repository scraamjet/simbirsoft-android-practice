package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentSearchByNkoBinding
import dev.androidbroadcast.vbpd.viewBinding

class SearchNKOFragment : Fragment(R.layout.fragment_search_by_nko) {
    private val binding by viewBinding(FragmentSearchByNkoBinding::bind)

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
            adapter = EventAdapter(generateEventsList())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun generateEventsList(): List<Event> {
        val events =
            listOf(
                Event("Мастер-класс по рисованию", R.drawable.icon_chevron_right),
                Event("Благотворительный концерт", R.drawable.icon_chevron_right),
                Event("Экологическая акция", R.drawable.icon_chevron_right),
                Event("Лекция по здоровью", R.drawable.icon_chevron_right),
                Event("Волонтерская помощь", R.drawable.icon_chevron_right),
            )
        return events
    }

    companion object {
        fun newInstance() = SearchNKOFragment()
    }
}
