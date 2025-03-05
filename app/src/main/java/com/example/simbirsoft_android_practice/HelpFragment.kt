package com.example.simbirsoft_android_practice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_help, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.help_item_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = HelpAdapter(getHelpCategories())
    }

    private fun getHelpCategories(): List<HelpCategory> {
        return listOf(
            HelpCategory("Дети", R.drawable.help_children),
            HelpCategory("Животные", R.drawable.help_animals),
            HelpCategory("Пожилые", R.drawable.help_old),
            HelpCategory("Животные", R.drawable.help_animals),
            HelpCategory("Мероприятия", R.drawable.help_events)
        )
    }

    companion object {
        fun newInstance() = HelpFragment()
    }
}