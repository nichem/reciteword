package com.example.reciteword.ui.pages

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.reciteword.Repository
import com.example.reciteword.database.entity.BookID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(actions: AppActions) {
    var curBookID by remember {
        mutableStateOf(BookID.CET4_1)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = curBookID.bookName, fontSize = 16.sp)
            }, actions = {
                IconButton(onClick = actions.toSetting) {
                    Icon(Icons.Default.Settings, contentDescription = "")
                }
            })
        }
    ) {
        LaunchedEffect(key1 = Unit, block = {
            curBookID = Repository.currentBookID
        })
        Box(modifier = Modifier.padding(it)) {

        }
    }
}