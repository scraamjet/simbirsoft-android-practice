package com.example.core.navigation

import androidx.navigation.NavController

interface AppRouter {
    fun navigateToEditPhotoDialog(navController: NavController)
    fun navigateToNewsDetail(navController: NavController, newsId: Int)
    fun navigateToFilter(navController: NavController)
    fun navigateToHelp(navController: NavController)
}
