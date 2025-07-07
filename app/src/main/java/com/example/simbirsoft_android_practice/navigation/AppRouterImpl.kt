package com.example.simbirsoft_android_practice.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.core.navigation.AppRouter
import com.example.simbirsoft_android_practice.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val NEWS_ID_KEY = "newsId"

class AppRouterImpl @Inject constructor() : AppRouter {

    private val _bottomNavVisibilityFlow: MutableStateFlow<Boolean> = MutableStateFlow(true)
    override val bottomNavVisibilityFlow: StateFlow<Boolean> = _bottomNavVisibilityFlow.asStateFlow()

    override fun setBottomNavigationVisible(visible: Boolean) {
        _bottomNavVisibilityFlow.value = visible
    }

    override fun navigateToNewsDetail(navController: NavController, newsId: Int) {
        val bundle = bundleOf(NEWS_ID_KEY to newsId)
        navController.navigate(R.id.action_news_to_news_detail, bundle)
    }

    override fun navigateToFilter(navController: NavController) {
        navController.navigate(R.id.action_news_to_filter)
    }

    override fun navigateToEditPhotoDialog(navController: NavController) {
        navController.navigate(R.id.action_profile_to_edit_photo_dialog)
    }

    override fun navigateToHelp(navController: NavController) {
        navController.navigate(R.id.action_authorization_to_help)
    }
}

