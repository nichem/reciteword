package com.example.worddb

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.worddb.database.AppDatabase
import com.example.worddb.database.dao.WordDao
import com.example.worddb.database.entity.BookID
import com.example.worddb.database.entity.Word
import com.example.worddb.utils.Common.getNeedReviewDay
import com.example.worddb.utils.Common.getNowDay
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class WordManager(private val context: Context) {

    init {
        MMKV.initialize(context)
    }

    companion object {
        private const val APP_DATABASE_NAME = "tmp.db"
        private const val PAGE_SIZE = 10
    }

    private lateinit var db: AppDatabase
    private lateinit var wordDao: WordDao

    suspend fun getWords(): List<Word> {
        return withContext(IO) {
            wordDao.getWords()
        }
    }


    suspend fun getReciteWords(bookID: BookID?): List<Word> {
        if (bookID == null) return emptyList()
        return withContext(IO) {
            val today = getNowDay()
            val needReview = wordDao.getNeedReviewWords(bookID, today)
            val notRecite = wordDao.getNotReciteWords(bookID)
            Log.d("test", "needReview：${needReview.size} notRecite：${notRecite.size}")
            needReview + notRecite
        }
    }


    suspend fun rememberWord(word: Word) {
        val reviewDay = getNeedReviewDay(word.rememberLevel)
        word.status = 1
        word.rememberLevel++
        word.nextTime = reviewDay
        withContext(IO) {
            wordDao.update(word)
        }
    }

    suspend fun forgetWord(word: Word) {
        val reviewDay = getNeedReviewDay(0)
        word.status = 1
        word.rememberLevel = 1
        word.nextTime = reviewDay
        withContext(IO) {
            wordDao.update(word)
        }
    }

    /**
     * 对某个单词有点印象
     */
    suspend fun normalWord(word: Word) {
        val level = Math.min(word.rememberLevel, 1)
        val reviewDay = getNeedReviewDay(level)
        word.status = 1
        word.rememberLevel = level + 1
        word.nextTime = reviewDay
        withContext(IO) {
            wordDao.update(word)
        }
    }

    suspend fun initDatabase() {
        val dir = File(context.dataDir, "databases").apply {
            if (!exists()) mkdir()
        }
        val fileNames = context.assets.list("")?.filter {
            APP_DATABASE_NAME in it
        } ?: emptyList()
        Log.d("test", "${fileNames.toList()}")
        fileNames.forEach {
            val file = File(dir, it)
            if (file.exists()) return@forEach //continue
            val ins = context.assets.open(it)
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
        db = Room.databaseBuilder(context, AppDatabase::class.java, APP_DATABASE_NAME)
            .build()
        wordDao = db.getWordDao()
    }


    private val mmkv = MMKV.defaultMMKV()
    var currentBookID: BookID
        get() {
            val string = mmkv.decodeString("currentBookID") ?: BookID.CET4_1.toString()
            return BookID.valueOf(string)
        }
        set(value) {
            mmkv.encode("currentBookID", value.toString())
        }

}