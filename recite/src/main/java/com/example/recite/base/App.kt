package com.example.recite.base

import android.annotation.SuppressLint
import android.app.Application
import com.example.worddb.WordManager

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var wordManager: WordManager
    }

    override fun onCreate() {
        super.onCreate()
        wordManager = WordManager(this)
    }
}