package com.example.reciteword.ui.pages

import androidx.navigation.NavController

class AppActions(navController: NavController) {
    val toMain: () -> Unit = {
        navController.navigate(AppDestinations.HOME_PAGE_ROUTE) {
            popUpTo(AppDestinations.SPLASH_PAGE_ROUTE) {
                inclusive = true
            }
        }
    }

    val toSetting: () -> Unit = {
        navController.navigate(AppDestinations.SETTING_PAGE_ROUTE)
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}