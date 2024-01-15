package com.example.reciteword

import android.annotation.SuppressLint
import android.app.Application
import com.example.worddb.WordManager
import com.tencent.mmkv.MMKV

class App : Application() {
    companion object {
        lateinit var app: App
        lateinit var wordManager: WordManager
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        MMKV.initialize(this)
        wordManager = WordManager(this)
    }
}