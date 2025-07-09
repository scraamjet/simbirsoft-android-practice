package com.example.simbirsoft_android_practice

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

interface AppRouter {
    val bottomNavVisibility: StateFlow<Boolean>
    fun setBottomNavigationVisible(visible: Boolean)
    fun navigateToEditPhotoDialog(navController: NavController)
    fun navigateToNewsDetail(navController: NavController, newsId: Int)
    fun navigateToFilter(navController: NavController)
    fun navigateToHelp(navController: NavController)
}
