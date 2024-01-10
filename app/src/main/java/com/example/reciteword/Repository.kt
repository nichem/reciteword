package com.example.reciteword

import android.util.Log
import androidx.room.Room
import com.example.reciteword.App.Companion.app
import com.example.reciteword.database.AppDatabase
import com.example.reciteword.database.dao.WordDao
import com.example.reciteword.database.entity.Word
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object Repository {
    private const val APP_DATABASE_NAME = "tmp.db"

    private lateinit var db: AppDatabase
    private lateinit var wordDao: WordDao

    suspend fun getWords(): List<Word> {
        return withContext(IO) {
            wordDao.getWords()
        }
    }

    suspend fun initDatabase() {
        val dir = File(app.dataDir, "databases").apply {
            if (!exists()) mkdir()
        }
        val fileNames = app.assets.list("")?.filter {
            APP_DATABASE_NAME in it
        } ?: emptyList()
        Log.d("test", "${fileNames.toList()}")
        fileNames.forEach {
            val file = File(dir, it)
            if (file.exists()) return@forEach //continue
            val ins = app.assets.open(it)
            val buffer = ByteArray(2048)
            withContext(IO) {
                val outs = FileOutputStream(file)
                while (true) {
                    val count = ins.read(buffer)
                    if (count < 0) break
                    outs.write(buffer, 0, count)
                }
                outs.flush()
                outs.close()
            }
        }
        db = Room.databaseBuilder(app, AppDatabase::class.java, APP_DATABASE_NAME)
            .build()
        wordDao = db.getWordDao()
    }

}