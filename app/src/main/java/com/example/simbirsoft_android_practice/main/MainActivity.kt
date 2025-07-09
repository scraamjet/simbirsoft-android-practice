package com.example.simbirsoft_android_practice.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.simbirsoft_android_practice.MultiViewModelFactory
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.appComponent
import com.example.simbirsoft_android_practice.databinding.ActivityMainBinding
import com.example.simbirsoft_android_practice.launchInLifecycle
import com.example.simbirsoft_android_practice.news.EventService
import com.example.simbirsoft_android_practice.news.EventServiceConnection
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val mainViewModel by viewModels<MainViewModel> { viewModelFactory }

    private var eventService: EventService? = null
    private var isServiceConnected = false

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private val connection =
        EventServiceConnection(
            onServiceConnected = { service ->
                eventService = service
                isServiceConnected = true
                observeEventsFromService()
            },
            onServiceDisconnected = {
                isServiceConnected = false
            }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setupNavigation()
        observeBottomNavigationVisibility()
        observeBadgeCount()
    }

    private fun observeEventsFromService() {
        eventService?.let { service ->
            lifecycleScope.launch {
                service.loadEvents()
                    .collect { eventList ->
                        mainViewModel.updateEventsFromService(eventList)
                    }
            }
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authorizationFragment,
                R.id.newsDetailFragment -> mainViewModel.setBottomNavigationVisible(false)

                else -> mainViewModel.setBottomNavigationVisible(true)
            }
        }
    }

    private fun observeBottomNavigationVisibility() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            mainViewModel.bottomNavigationVisible.collect { visible ->
                if (visible) {
                    showBottomNavigation()
                } else {
                    hideBottomNavigation()
                }
            }
        }
    }

    private fun observeBadgeCount() {
        launchInLifecycle(Lifecycle.State.STARTED) {
            mainViewModel.badgeFlow.collect { count: Int ->
                updateUnreadNewsBadge(count)
            }
        }
    }

    fun startAndBindEventService() {
        val intent = Intent(this, EventService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isServiceConnected) {
            unbindService(connection)
            isServiceConnected = false
        }
    }

    private fun updateUnreadNewsBadge(count: Int) {
        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.newsFragment)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }

    private fun hideBottomNavigation() {
        binding.bottomNavigationView.clearAnimation()
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }
}
