package com.example.reciteword.ui.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage(actions: AppActions) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "设置") },
                navigationIcon = {
                    IconButton(onClick = actions.upPress) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "")
                    }
                })
        }
    ) {
        Column(Modifier.fillMaxSize()) {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "选择词书")
            }
        }
    }
}