package com.example.simbirsoft_android_practice.navigation

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.example.core.navigation.AppRouter
import com.example.simbirsoft_android_practice.R
import javax.inject.Inject

class AppRouterImpl @Inject constructor() : AppRouter {

    override fun navigateToNewsDetail(navController: NavController, newsId: Int) {
        val bundle = bundleOf("newsId" to newsId)
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

