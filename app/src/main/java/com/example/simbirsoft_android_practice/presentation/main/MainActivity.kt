package com.example.simbirsoft_android_practice.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.core.di.MultiViewModelFactory
import com.example.core.navigation.AppRouter
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.databinding.ActivityMainBinding
import com.example.simbirsoft_android_practice.di.appComponent
import com.example.simbirsoft_android_practice.presentation.service.NewsService
import com.example.simbirsoft_android_practice.presentation.service.NewsServiceConnection
import com.example.simbirsoft_android_practice.presentation.service.NewsServiceProxy
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    @Inject
    lateinit var viewModelFactory: MultiViewModelFactory

    private val mainViewModel by viewModels<MainViewModel> { viewModelFactory }

    @Inject
    lateinit var appRouter: AppRouter

    private var newsService: NewsService? = null
    private var isServiceConnected = false

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private val newsServiceProxy: NewsServiceProxy by lazy { NewsServiceProxy() }

    private val connection =
        NewsServiceConnection(
            onServiceConnected = { connectedService ->
                newsService = connectedService
                isServiceConnected = true
                newsServiceProxy.setService(connectedService)
                mainViewModel.observeNews(newsServiceProxy)
            },
            onServiceDisconnected = {
                isServiceConnected = false
                newsServiceProxy.clearService()
            },
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setupNavigation()
        observeState()
        observeBottomNavVisibility()
        observeEffect()
    }

    private fun setupNavigation() {
        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.authorizationFragment,
                R.id.newsDetailFragment,
                    -> appRouter.setBottomNavigationVisible(false)

                else -> appRouter.setBottomNavigationVisible(true)
            }
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.state.collect { state: MainState ->
                    updateUnreadNewsBadge(count = state.badgeCount)
                }
            }
        }
    }

    private fun observeBottomNavVisibility() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                appRouter.bottomNavVisibilityFlow.collect { isVisible: Boolean ->
                    if (isVisible) {
                        showBottomNavigation()
                    } else {
                        hideBottomNavigation()
                    }
                }
            }
        }
    }

    private fun observeEffect() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.effect.collect { effect ->
                    when (effect) {
                        is MainEffect.StartAndBindNewsService -> startAndBindNewsService()
                    }
                }
            }
        }
    }

    private fun startAndBindNewsService() {
        val intent = Intent(this, NewsService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isServiceConnected) {
            unbindService(connection)
            isServiceConnected = false
            newsServiceProxy.clearService()
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
