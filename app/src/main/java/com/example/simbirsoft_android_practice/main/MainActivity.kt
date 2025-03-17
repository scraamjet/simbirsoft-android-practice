package com.example.simbirsoft_android_practice.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.simbirsoft_android_practice.news.NewsFragment
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.ActivityMainBinding
import com.example.simbirsoft_android_practice.help.HelpFragment
import com.example.simbirsoft_android_practice.profile.ProfileFragment
import com.example.simbirsoft_android_practice.search.SearchContainerFragment
import dev.androidbroadcast.vbpd.viewBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initHelpFragment(savedInstanceState)
        setupBottomNavigation()
    }

    private fun initHelpFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loadFragment(HelpFragment.newInstance())
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.selectedItemId = R.id.help

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.help -> loadFragment(HelpFragment.newInstance())
                R.id.profile -> loadFragment(ProfileFragment.newInstance())
                R.id.search -> loadFragment(SearchContainerFragment.newInstance())
                R.id.news -> loadFragment(NewsFragment.newInstance())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayoutFragmentContainer, fragment)
            .commit()
    }

    fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}
