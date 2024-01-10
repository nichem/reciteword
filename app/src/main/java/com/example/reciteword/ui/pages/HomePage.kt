package com.example.reciteword.ui.pages

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.reciteword.Repository

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(actions: AppActions) {
    Scaffold(
        topBar = {
            TopAppBar(title = {}, actions = {
                IconButton(onClick = actions.toSetting) {
                    Icon(Icons.Default.Settings, contentDescription = "")
                }
            })
        }
    ) {
        LaunchedEffect(key1 = Unit, block = {
            val words = Repository.getWords()
            Log.d("test", "size:${words.size}")
        })
    }
}