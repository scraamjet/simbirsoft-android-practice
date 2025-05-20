package com.example.simbirsoft_android_practice.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.auth.AuthorizationFragment
import com.example.simbirsoft_android_practice.data.NewsItem
import com.example.simbirsoft_android_practice.databinding.ActivityMainBinding
import com.example.simbirsoft_android_practice.filter.FilterPreferences
import com.example.simbirsoft_android_practice.help.HelpFragment
import com.example.simbirsoft_android_practice.news.NewsFragment
import com.example.simbirsoft_android_practice.news.NewsMapper
import com.example.simbirsoft_android_practice.news.NewsPreferences
import com.example.simbirsoft_android_practice.news.NewsService
import com.example.simbirsoft_android_practice.news.NewsServiceConnection
import com.example.simbirsoft_android_practice.profile.ProfileFragment
import com.example.simbirsoft_android_practice.search.SearchContainerFragment
import dev.androidbroadcast.vbpd.viewBinding
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG_MAIN_ACTIVITY = "MainActivity"

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

    private val filterPrefs by lazy { FilterPreferences(this) }
    private val newsPrefs by lazy { NewsPreferences(this) }

    private var newsService: NewsService? = null
    private var isServiceConnected = false

    private val unreadNewsCount = MutableStateFlow(0)

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Log.e(TAG_MAIN_ACTIVITY, "Flow exception: ${throwable.localizedMessage}", throwable)
        }

    private val connection =
        NewsServiceConnection(
            onServiceConnected = { service ->
                newsService = service
                isServiceConnected = true
                loadAndUpdateUnreadNewsCount()
            },
            onServiceDisconnected = {
                isServiceConnected = false
            },
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAuthorizationFragment(savedInstanceState)
        setupBottomNavigation()
    }

    fun startAndBindNewsService() {
        val intent = Intent(this, NewsService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if (isServiceConnected) {
            unbindService(connection)
            isServiceConnected = false
        }
    }

    fun loadAndUpdateUnreadNewsCount() {
        val service = newsService ?: return

        lifecycleScope.launch(coroutineExceptionHandler) {
            service.loadNews()
                .collect { loadedNews ->
                    val selectedCategories = filterPrefs.getSelectedCategories()
                    val filteredNewsItems =
                        loadedNews
                            .filter { news -> news.categoryIds.any { categoryId -> categoryId in selectedCategories } }
                            .map(NewsMapper::eventToNewsItem)

                    updateUnreadNewsCount(filteredNewsItems)
                }
        }
    }

    private fun updateUnreadNewsCount(newsList: List<NewsItem>) {
        lifecycleScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val readNewsIds = newsPrefs.getReadNewsIds()
            val unreadCount = newsList.count { newsItem -> newsItem.id !in readNewsIds }

            unreadNewsCount.value = unreadCount

            withContext(Dispatchers.Main) {
                updateUnreadNewsBadge(unreadCount)
            }
        }
    }

    private fun initAuthorizationFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            loadFragment(AuthorizationFragment.newInstance(), addToBackStack = false)
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

    private fun loadFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true,
    ) {
        val transaction =
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutFragmentContainer, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }

    fun hideBottomNavigation() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun updateUnreadNewsBadge(count: Int) {
        val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.news)
        if (count > 0) {
            badge.isVisible = true
            badge.number = count
        } else {
            badge.isVisible = false
        }
    }
}
