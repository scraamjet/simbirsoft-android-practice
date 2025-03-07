package com.example.simbirsoft_android_practice.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.ActivityMainBinding
import com.example.simbirsoft_android_practice.help.HelpFragment
import com.example.simbirsoft_android_practice.profile.ProfileFragment
import com.example.simbirsoft_android_practice.search.SearchFragment
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
        binding.bottomNavView.selectedItemId = R.id.help

        binding.bottomNavView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.help -> loadFragment(HelpFragment.newInstance())
                R.id.profile -> loadFragment(ProfileFragment.newInstance())
                R.id.search -> loadFragment(SearchFragment.newInstance())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
