package com.example.reciteword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.reciteword.ui.common.Loading
import com.example.reciteword.ui.pages.AppActions
import com.example.reciteword.ui.pages.AppDestinations
import com.example.reciteword.ui.pages.HomePage
import com.example.reciteword.ui.pages.SettingPage
import com.example.reciteword.ui.pages.SplashPage
import com.example.reciteword.ui.theme.RecitewordTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecitewordTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen() {
    val navController = rememberNavController()
    val actions = remember(navController) {
        AppActions(navController)
    }
    NavHost(
        navController = navController,
        startDestination = AppDestinations.SPLASH_PAGE_ROUTE,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(AppDestinations.SPLASH_PAGE_ROUTE) {
            SplashPage(actions)
        }

        composable(AppDestinations.HOME_PAGE_ROUTE) {
            HomePage(actions)
        }

        composable(AppDestinations.SETTING_PAGE_ROUTE) {
            SettingPage(actions)
        }
    }
}