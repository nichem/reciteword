package com.example.reciteword.ui.pages

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.reciteword.Repository
import com.example.reciteword.database.entity.BookID
import com.example.reciteword.database.entity.Word
import com.example.reciteword.ui.common.Loading
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(actions: AppActions) {
    var curBookID: BookID? by rememberSaveable {
        mutableStateOf(null)
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = curBookID?.bookName ?: "", fontSize = 16.sp)
            }, actions = {
                IconButton(onClick = actions.toSetting) {
                    Icon(Icons.Default.Settings, contentDescription = "")
                }
            })
        }
    ) {
        val viewModel: HomeViewModel = viewModel()
        val words by viewModel.words.observeAsState()
        var wordIndex by rememberSaveable {
            mutableStateOf(-1)
        }
        var loading by remember {
            mutableStateOf(false)
        }
        if (loading) {
            Loading(msg = "初始化") {}
        }
        LaunchedEffect(key1 = Unit, block = {
            Log.d("test", curBookID?.bookName ?: "")
            if (curBookID != Repository.currentBookID) {
                Log.d("test", "${curBookID?.bookName} ${Repository.currentBookID}")
                curBookID = Repository.currentBookID
                loading = true
                viewModel.onWordsChanged(Repository.getReciteWords(curBookID))
                wordIndex = 0
                loading = false
            }
        })
        val scope = rememberCoroutineScope()
        Box(modifier = Modifier.padding(it)) {
            Column(Modifier.padding(5.dp)) {
                WordCard(
                    word = words?.getOrNull(wordIndex), modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Row {
                    TextButton(onClick = { wordIndex++ }, modifier = Modifier.weight(1f)) {
                        Text(text = "不记得")
                    }
                    TextButton(onClick = { wordIndex++ }, modifier = Modifier.weight(1f)) {
                        Text(text = "有印象")
                    }
                    TextButton(onClick = { wordIndex++ }, modifier = Modifier.weight(1f)) {
                        Text(text = "我知道")
                    }
                }
            }
        }
    }
}

@Composable
fun WordCard(word: Word?, modifier: Modifier) {
    Card(modifier = modifier) {
        if (word != null) {
            Column {
                Text(text = word.bookID.bookName)
                Text(text = word.text)
            }
        }
    }
}

class HomeViewModel : ViewModel() {
    val words = MutableLiveData(emptyList<Word>())
    fun onWordsChanged(words: List<Word>) {
        this.words.postValue(words)
    }
}