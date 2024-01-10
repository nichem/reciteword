package com.example.reciteword.ui.pages

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val toSetting: () -> Unit = {
        navController.navigate(AppDestinations.SETTING_PAGE_ROUTE)
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}