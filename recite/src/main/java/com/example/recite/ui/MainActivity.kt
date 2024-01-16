package com.example.recite.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ActivityUtils
import com.example.recite.R
import com.example.recite.base.App.Companion.wordManager
import com.example.recite.base.BaseActivity
import com.example.recite.databinding.ActivityMainBinding
import com.example.worddb.database.entity.BookID
import com.example.worddb.database.entity.Word
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.actionbar.TitleBar.ImageAction
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun createBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        viewModel.word.observe(this) {
            setWord(it)
        }
        viewModel.stateText.observe(this) {
            getTitleBar().setSubTitle(it)
        }
        binding.btnForget.setOnClickListener {
            viewModel.operateWord(MainViewModel.Operation.Forget)
        }
        binding.btnNormal.setOnClickListener {
            viewModel.operateWord(MainViewModel.Operation.Normal)
        }
        binding.btnRemember.setOnClickListener {
            viewModel.operateWord(MainViewModel.Operation.Remember)
        }
        viewModel.initReciteWords()
    }

    override fun initTitleBar(bar: TitleBar) {
        super.initTitleBar(bar)
        bar.setTitle("背单词")
            .disableLeftView()
            .addAction(object : ImageAction(R.drawable.baseline_settings_24) {
                override fun performAction(view: View?) {
                    //toSetting
                    ActivityUtils.startActivity(SettingActivity::class.java)
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setWord(word: Word?) {
        if (word == null) return
        binding.tvWord.text = word.text
        binding.tvPhonetic.text = "[美]${word.usPhonetic} [英]${word.ukPhonetic}"
        binding.tvTransCn.text = word.tranCN
        binding.tvTransOther.text = word.tranOther
        binding.tvSentence.text = word.sentence
        binding.tvSentenceCn.text = word.sentenceCN
    }

    override fun onResume() {
        super.onResume()
        viewModel.initReciteWords()
    }

}

class MainViewModel() : ViewModel() {
    private var needReviewWords: List<Word> = emptyList()
    private var noReciteWords: List<Word> = emptyList()
    private val _wordIndex = MutableLiveData<Int>()
    val word: LiveData<Word?> = _wordIndex.map {
        getWord(it)
    }

    val stateText: LiveData<String> = _wordIndex.map {
        val leftCount = needReviewWords.size - it
        if (leftCount > 0) {
            "今日还需复习${leftCount}个单词"
        } else
            "还剩${noReciteWords.size - (it - needReviewWords.size)}个单词要学习"
    }

    fun initReciteWords() = viewModelScope.launch {
        noReciteWords = wordManager.getNotReciteWords(wordManager.currentBookID)
        needReviewWords = wordManager.getNeedReviewWords(wordManager.currentBookID)
        _wordIndex.postValue(0)
    }

    enum class Operation {
        Forget, Normal, Remember
    }

    fun operateWord(operation: Operation) {
        val word = word.value ?: return
        viewModelScope.launch {
            when (operation) {
                Operation.Forget -> {
                    wordManager.forgetWord(word)
                }

                Operation.Normal -> {
                    wordManager.normalWord(word)
                }

                Operation.Remember -> {
                    wordManager.rememberWord(word)
                }
            }
            _wordIndex.postValue((_wordIndex.value ?: -1) + 1)
        }
    }

    private fun getWord(index: Int): Word? {
        return if (index < 0) null
        else if (index < needReviewWords.size)
            needReviewWords[index]
        else if (index < needReviewWords.size + noReciteWords.size)
            noReciteWords[index - needReviewWords.size]
        else null
    }

}