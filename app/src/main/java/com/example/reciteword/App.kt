package com.example.reciteword

import android.app.Application
import com.tencent.mmkv.MMKV

class App : Application() {
    companion object {
        lateinit var app: App
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        MMKV.initialize(this)
    }
}