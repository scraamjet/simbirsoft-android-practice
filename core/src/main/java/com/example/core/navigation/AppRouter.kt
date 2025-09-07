package com.example.core.navigation

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

interface AppRouter {
    val bottomNavVisibility: StateFlow<Boolean>
    fun setBottomNavigationVisible(visible: Boolean)
    fun navigateToEditPhotoDialog(navController: NavController)
    fun navigateToNewsDetail(navController: NavController, newsId: Int)
    fun navigateToFilter(navController: NavController)
    fun navigateToHelp(navController: NavController)
    fun navigateToNews(navController: NavController)
    fun navigateToDonateDialog(navController: NavController, newsId: Int, newsTitle: String)
}
