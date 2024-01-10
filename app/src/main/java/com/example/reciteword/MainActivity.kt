package com.example.reciteword

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.reciteword.ui.dialog.Loading
import com.example.reciteword.ui.theme.RecitewordTheme
import kotlinx.coroutines.delay

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
                    LoadingDatabaseDialog()
                }
            }
        }
    }
}

@Composable
fun LoadingDatabaseDialog() {
    var loadingDatabase by remember {
        mutableStateOf(true)
    }
    Loading(msg = "加载数据库中...", isLoading = loadingDatabase)
    LaunchedEffect(key1 = Unit, block = {
        Repository.init()
        loadingDatabase = false
    })
}