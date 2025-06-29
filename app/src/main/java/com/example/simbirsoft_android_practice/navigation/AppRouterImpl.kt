package com.example.simbirsoft_android_practice.navigation

import androidx.navigation.NavController
import com.example.core.navigation.AppRouter
import com.example.simbirsoft_android_practice.R
import com.example.simbirsoft_android_practice.presentation.news.NewsFragmentDirections
import javax.inject.Inject

class AppRouterImpl @Inject constructor() : AppRouter {

    override fun navigateToEditPhotoDialog(navController: NavController) {
        navController.navigate(R.id.action_profile_to_edit_photo_dialog)
    }

    override fun navigateToNewsDetail(navController: NavController, newsId: Int) {
        val action = NewsFragmentDirections.actionNewsToNewsDetail(newsId)
        navController.navigate(action)
    }

    override fun navigateToFilter(navController: NavController) {
        navController.navigate(R.id.action_news_to_filter)
    }

    override fun navigateToHelp(navController: NavController) {
        navController.navigate(R.id.action_authorization_to_help)
    }
}
