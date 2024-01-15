package com.example.reciteword.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reciteword.App.Companion.wordManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashPage(actions: AppActions) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var loadingDatabase by remember {
            mutableStateOf(false)
        }
        if (loadingDatabase) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .defaultMinSize(100.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .width(40.dp)
                            .height(40.dp)
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(text = "应用初次进入需要初始化")
                }
            }
        }
        LaunchedEffect(key1 = Unit, block = {
            launch {
                delay(500)
                loadingDatabase = true
            }
            wordManager.initDatabase()
            loadingDatabase = false
            actions.toMain()
        })
    }
}