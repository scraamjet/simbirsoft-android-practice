package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.simbirsoft_android_practice.databinding.FragmentHelpBinding
import dev.androidbroadcast.vbpd.viewBinding

private const val ITEM_DECORATOR_SPAN_COUNT = 2

class HelpFragment : Fragment(R.layout.fragment_help) {
    private val binding by viewBinding(FragmentHelpBinding::bind)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = binding.helpItemRecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = HelpAdapter(getHelpCategories())
        recyclerView.addItemDecoration(
            GridSpacingItemDecoration(
                ITEM_DECORATOR_SPAN_COUNT,
                resources.getDimensionPixelSize(R.dimen.recycler_view_spacing),
                true,
            ),
        )
    }

    private fun getHelpCategories(): List<HelpCategory> {
        return listOf(
            HelpCategory("Дети", R.drawable.help_children),
            HelpCategory("Взрослые", R.drawable.help_adults),
            HelpCategory("Пожилые", R.drawable.help_old),
            HelpCategory("Животные", R.drawable.help_animals),
            HelpCategory("Мероприятия", R.drawable.help_events),
        )
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}
