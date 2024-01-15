package com.example.reciteword.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reciteword.App.Companion.wordManager
import com.example.worddb.database.entity.BookID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingPage(actions: AppActions) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "设置", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = actions.upPress) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "")
                    }
                })
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            Column(
                Modifier
                    .fillMaxSize()
            ) {
                BookIDSetting()
            }
        }
    }
}

@Composable
fun BookIDSetting() {
    var curBookID by remember {
        mutableStateOf(BookID.CET4_1)
    }
    var isShow by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit, block = {
        curBookID = wordManager.currentBookID
    })
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                isShow = true
            }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "词书", Modifier.weight(1f))
        Text(text = curBookID.bookName, Modifier.padding(horizontal = 5.dp), fontSize = 14.sp)
        Icon(Icons.Default.ArrowForward, contentDescription = "")
    }

    if (isShow) {
        Dialog(
            onDismissRequest = { isShow = false }
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .requiredHeight(400.dp)
            ) {
                LazyColumn(modifier = Modifier.padding(vertical = 5.dp), content = {
                    items(enumValues<BookID>()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .clickable {
                                    curBookID = it
                                    isShow = false
                                    wordManager.currentBookID = it
                                }
                                .fillMaxWidth()
                        ) {
                            RadioButton(selected = curBookID == it, onClick = {
                                curBookID = it
                                wordManager.currentBookID = it
                            })
                            Text(text = "《${it.bookName}》")
                        }
                    }
                })
            }
        }
    }
}

